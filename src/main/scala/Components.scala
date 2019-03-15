import com.diogosantos.github.{FetchUserRepositoriesCommand, GithubClient}
import github4s.Github
import scala.concurrent.ExecutionContext

object Components {

  private val githubToken = Option(sys.env("GITHUB_APP_TOKEN"))

  private val githubReposApi = Github(githubToken).repos
  private val githubExecutionContext: ExecutionContext = ExecutionContext.fromExecutorService(null)
  private val githubClient = new GithubClient(githubReposApi)(githubExecutionContext)

  val fetchUserRepositoriesCommand: FetchUserRepositoriesCommand = new FetchUserRepositoriesCommand(githubClient)

}
