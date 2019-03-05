import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object AddPet {

  val createPetFeeder = csv("petsList.csv").random

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded")

  def createPet (thinkTime : Int):ChainBuilder = exec(http("Add pet")
    .get("/owners/${ownerID}/pets/new")
    .check(status.is(200)))
    .feed(createPetFeeder)
    .pause(thinkTime)
    .exec(http("Send pet's form")
      .post("/owners/${ownerID}/pets/new")
      .headers(headers_10)
      .formParam("name", "${petName}")
      .formParam("birthDate", "${birthDate}")
      .formParam("type", "${petType}")
      .check(status.is(200))
      .check(regex("""href="${ownerID}/pets/(\d+)/visits/new">Add""").saveAs("petID")))
}