import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Browse {
  def browse (thinkTime : Int):ChainBuilder = exec(http("Home")
    .get("/")
    .check(status.is(200)))
}