package com.diogosantos.github
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class GithubUserService(githubClient: GithubClient) {

  import scala.concurrent.ExecutionContext.Implicits.global

  def run(username: String): Unit = {
    val futureEitherRepos: Future[Either[GitHubError, List[String]]] =
      githubClient.fetchRepositoriesNames(username)

    val eventualRepoNames: Future[List[String]] =
      for (eitherRepos <- futureEitherRepos)
        yield
          eitherRepos match {
            case Right(names) => names
            case Left(e) =>
              println(s"Error retrieving the repositories: ${e.message}")
              List()
          }

    val repoNames = Await.result(eventualRepoNames, Duration.Inf)

    val eventualRepoInfos = repoNames.map(repo => fetchContributors(username, repo))
    val eventualResults: Future[List[RepoInfo]] = Future.sequence(eventualRepoInfos)

    val result = Await.result(eventualResults, Duration.Inf)

    result.foreach { repoInfo =>
      println(repoInfo.repo)
      println(s"\t${repoInfo.contribs.mkString(",")}")
    }
  }

  private def fetchContributors(username: String, repo: String) = {
    val eventualEitherContribs: Future[Either[GitHubError, List[String]]] =
      githubClient.fetchRepositoryContributors(username, repo)

    for (eitherContribs <- eventualEitherContribs)
      yield
        eitherContribs match {
          case Right(contribs) => RepoInfo(repo, contribs)
          case Left(e) =>
            println(
              s"Error retrieving the contributors for repo `$repo`: ${e.message}"
            )
            RepoInfo(repo, List())
        }
  }
}

case class RepoInfo(repo: String, contribs: List[String])
