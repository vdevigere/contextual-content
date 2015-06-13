package org.target.servlet

import org.scalatra.ScalatraServlet

class CampaignServlet extends ScalatraServlet {

  get("/campaigns") {
    <html>
      <body>
        <h1>Hello, Viddu!</h1>
        Say
        <a href="hello-scalate">hello to Scalate</a>
        .
      </body>
    </html>
  }

  post("/campaigns") {
    "creating a campaign"
  }

  get("/campaigns/:id") {
    "fetching campaign:"+params("id")
  }

  put("/campaigns/:id") {
    "updating campaign:"+params("id")
  }
}
