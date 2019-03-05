object DummyHealth //Used to enable steps usage

package simulations {

  import java.io.File
  import java.util.concurrent.ThreadLocalRandom
  import com.typesafe.config.ConfigFactory

  import scala.concurrent.duration._
  import io.gatling.core.Predef._
  import io.gatling.http.Predef._
  import io.gatling.jdbc.Predef._

  class HealthCheckSimulation extends Simulation {

    //Load simulation parameters
    val config = ConfigFactory.parseFile(new File(getClass().getResource("../../simulation-properties.conf").getPath))

    val url = config.getString("healthCheck.url")
    val users = config.getInt("healthCheck.users")
    val repetitions = config.getInt("healthCheck.repetitions")
    val thinkTime = config.getInt("healthCheck.thinkTime")

    val httpProtocol = http
      .baseURL(url)
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0")

    //scenarios
    val health = scenario("HealthCheck").repeat(repetitions) {
      exec(Browse.browse(thinkTime))
    }

    setUp(health.inject(atOnceUsers(users))).protocols(httpProtocol)
  }

}