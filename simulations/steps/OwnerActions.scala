import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object OwnerActions {

  val createOwnerFeeder = csv("ownersList.csv").random

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded")

  def createOwner (thinkTime : Int):ChainBuilder = {
    exec(http("Add owner")
      .get("/owners/new")
      .check(status.is(200)))
      .feed(createOwnerFeeder)
      .pause(thinkTime)
      .exec(http("Send owner's form")
        .post("/owners/new")
        .headers(headers_10)
        .formParam("firstName", "${firstName}")
        .formParam("lastName", "${lastName}")
        .formParam("address", "${address}")
        .formParam("city", "${city}")
        .formParam("telephone", "${telephone}")
        .check(status.is(200))
        .check(currentLocation.transform(url => {
          val pattern = """\d+$""".r
          val matcher = pattern findFirstIn url
          matcher.get
        }) saveAs ("ownerID")))
  }

  def search (thinkTime : Int):ChainBuilder = {
    exec(http("Search bar")
      .get("/owners/find")
      .check(status.is(200)))
      .feed(createOwnerFeeder)
      .pause(thinkTime)
      .exec(http("Search owner")
        .get("/owners?lastName=${lastName}")
        .check(status.is(200))
        .check(regex("""<p>has not been found</p>""").count.lessThan(1))
        //checks if there are more than 1 result and takes the first
        .check(regex("""<a href="([^"]+)">${firstName}</a>""").optional.saveAs("ownerURL")))
      .pause(thinkTime)
      .doIf("${ownerURL.exists()}"){
        exec(http("Select Owner")
          .get("/${ownerURL}")
          .check(status.is(200)))
      }
  }
}