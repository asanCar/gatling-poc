object DummyNormal //Used to enable steps usage

package simulations {

  import java.io.File
  import java.util.concurrent.ThreadLocalRandom
  import com.typesafe.config.ConfigFactory

  import scala.concurrent.duration._
  import io.gatling.core.Predef._
  import io.gatling.http.Predef._
  import io.gatling.jdbc.Predef._

  class NormalUsageSimulation extends Simulation {

    //Load simulation parameters
    val config = ConfigFactory.parseFile(new File(getClass().getResource("../../simulation-properties.conf").getPath))

    val repetitions = config.getInt("normalUsage.repetitions")
    val expSet = config.getString("normalUsage.experiment")
    val url = config.getString("normalUsage.url")
    val users = config.getInt("normalUsage.experiment"+expSet+".users")
    val period = config.getLong("normalUsage.experiment"+expSet+".period")
    val thinkTime = config.getInt("normalUsage.experiment"+expSet+".thinkTime")

    val httpProtocol = http
      .baseURL(url)
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0")

    //scenarios
    val create = scenario("NormalUsage").repeat(repetitions) {
      exec(Browse.browse(thinkTime)).exec(OwnerActions.createOwner(thinkTime))
      .exec(AddPet.createPet(thinkTime)).exec(AddVisit.createVisit(thinkTime)).exec(OwnerActions.search(thinkTime))
    }

    setUp(
      create.inject(rampUsers(users) over (FiniteDuration(period, "seconds")))
    ).protocols(httpProtocol)
  }

}