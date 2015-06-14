package org.target.servlet

import org.infinispan.Cache
import org.infinispan.factories.annotations.Inject
import org.scalatra.ScalatraServlet
import org.target.db.CampaignDb

class CampaignServlet extends ScalatraServlet {

  get("/campaigns") {
    CampaignDb.readAll()
  }

  post("/campaigns") {
  }

  get("/campaigns/:id") {
    CampaignDb.read(params("id").toLong)
  }

  put("/campaigns/:id") {
    "updating campaign:" + params("id")
  }
}
