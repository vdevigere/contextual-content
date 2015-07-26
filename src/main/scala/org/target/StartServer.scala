package org.target

import com.google.inject.{AbstractModule, Guice}
import io.undertow.servlet.Servlets
import io.undertow.{Handlers, Undertow}
import org.slf4j.LoggerFactory
import org.target.db.{CampaignDb, RedisCampaignDb}
import org.target.servlet.CampaignServlet
import org.target.utils.ImplicitConversions._

/**
 * Created by Viddu on 6/12/2015.
 */
object StartServer extends App {
  val logger = LoggerFactory.getLogger(StartServer.getClass)
  val injector = Guice.createInjector(new AbstractModule() {
    override def configure(): Unit = {
      bind(classOf[CampaignDb]).to(classOf[RedisCampaignDb])
    }
  })

  val servletBuilder = Servlets.deployment()
    .setClassLoader(StartServer.getClass.getClassLoader)
    .setContextPath("/context")
    .setDeploymentName("context.war")
    .addServlets(
      Servlets.servlet("CampaignServlet", classOf[CampaignServlet], injector.getInstance(classOf[CampaignServlet])).addMapping("/*").setAsyncSupported(true)
    )
  val manager = Servlets.defaultContainer().addDeployment(servletBuilder)
  manager.deploy()

  val server = Undertow.builder()
    .addHttpListener(9001, "0.0.0.0")
    .setHandler(Handlers.path().addPrefixPath("/context", manager.start()))
    .build()
  server.start()
}
