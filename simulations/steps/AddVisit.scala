import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object AddVisit {

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded")

  def createVisit (thinkTime : Int):ChainBuilder = exec(http("Add pet's visit")
    .get("/owners/${ownerID}/pets/${petID}/visits/new")
    .check(status.is(200)))
    .pause(thinkTime)
    .exec(http("Send pet's form")
      .post("/owners/${ownerID}/pets/${petID}/visits/new")
      .headers(headers_10)
      .formParam("description", "Description")
      .check(status.is(200)))
}