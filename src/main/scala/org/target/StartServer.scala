package org.target

import com.google.inject.{AbstractModule, Guice, Singleton}
import com.typesafe.config.ConfigFactory
import io.undertow.servlet.Servlets
import io.undertow.{Handlers, Undertow}
import org.slf4j.LoggerFactory
import org.target.db.{CampaignDb, RedisCampaignDb}
import org.target.servlet.{CampaignServlet, ContentSelectorServlet}
import org.target.utils.ImplicitConversions._

/**
 * Created by Viddu on 6/12/2015.
 */
object StartServer extends App {
  val conf = ConfigFactory.load

  val logger = LoggerFactory.getLogger(StartServer.getClass)
  val injector = Guice.createInjector(new AbstractModule() {
    override def configure(): Unit = {
      bind(classOf[CampaignDb]).to(classOf[RedisCampaignDb]).in(classOf[Singleton])
      //bind(classOf[ObjectMapper]).toInstance(new ObjectMapper().registerModule(DefaultScalaModule))
    }
  })

  val servletBuilder = Servlets.deployment()
    .setClassLoader(StartServer.getClass.getClassLoader)
    .setContextPath("/context")
    .setDeploymentName("context.war")
    .addServlets(
      Servlets.servlet("CampaignServlet", classOf[CampaignServlet], injector.getInstance(classOf[CampaignServlet])).addMapping("/campaigns/*"),
      Servlets.servlet("ContentSelectorServlet", classOf[ContentSelectorServlet], injector.getInstance(classOf[ContentSelectorServlet])).addMapping("/content/*").setAsyncSupported(true)
    )
  val manager = Servlets.defaultContainer().addDeployment(servletBuilder)
  manager.deploy()

  val server = Undertow.builder()
    .addHttpListener(conf.getInt("sengi.port"), conf.getString("sengi.host"))
    .setHandler(Handlers.path().addPrefixPath("/context", manager.start()))
    .build()
  server.start()
}
