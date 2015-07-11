package org.target

import io.undertow.servlet.Servlets
import io.undertow.servlet.api.{InstanceHandle, InstanceFactory}
import io.undertow.{Handlers, Undertow}
import org.slf4j.LoggerFactory
import org.target.db.RedisCampaignDb
import org.target.servlet.CampaignServlet

/**
 * Created by Viddu on 6/12/2015.
 */
object StartServer extends App {
  val logger = LoggerFactory.getLogger(StartServer.getClass)

  val servletBuilder = Servlets.deployment()
    .setClassLoader(StartServer.getClass.getClassLoader)
    .setContextPath("/context")
    .setDeploymentName("context.war")
    .addServlets(
      Servlets.servlet("CampaignServlet", classOf[CampaignServlet], new InstanceFactory[CampaignServlet] {
        override def createInstance(): InstanceHandle[CampaignServlet] = {
          new InstanceHandle[CampaignServlet] {
            override def getInstance(): CampaignServlet = {
              logger.debug("Creating instance of CampaignServlet");
              new CampaignServlet(new RedisCampaignDb)
            }
            override def release(): Unit = {}
          }
        }
      }).addMapping("/*")
    )
  val manager = Servlets.defaultContainer().addDeployment(servletBuilder)
  manager.deploy()

  val server = Undertow.builder()
    .addHttpListener(9001, "0.0.0.0")
    .setHandler(Handlers.path().addPrefixPath("/context", manager.start()))
    .build()
  server.start()
}
