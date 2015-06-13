package org.target.servlet

import org.scalatra.ScalatraServlet

class CampaignServlet extends ScalatraServlet {

  get("/campaign") {
    <html>
      <body>
        <h1>Hello, Viddu!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }
}
