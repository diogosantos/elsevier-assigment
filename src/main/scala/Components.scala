import com.diogosantos.github.{GithubClient, FetchUserRepositoriesCommand}
import github4s.Github
object Components {

  private val githubToken = "6263a918c2298d670a105d089923205b10e7bbe3"

  private val githubReposApi = Github(Some(githubToken)).repos
  private val githubClient = new GithubClient(githubReposApi)

  val fetchUserRepositoriesCommand: FetchUserRepositoriesCommand = new FetchUserRepositoriesCommand(githubClient)

}
