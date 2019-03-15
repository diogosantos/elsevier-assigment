import scalaz.Scalaz._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main extends App {

  require(args.length > 0, "Parameter `username` is required.")
  val username = args(0)

  val fetchUserRepositoriesCommand = Components.fetchUserRepositoriesCommand

  val repos = for (repos <- fetchUserRepositoriesCommand.run(username))
    yield {
      repos.foreach { repo =>
        println(s"${repo.repo}")
        println(s"\t${repo.contributors.mkString(",")}")
      }
    }

  Await.ready(repos.run, Duration.Inf)
}
