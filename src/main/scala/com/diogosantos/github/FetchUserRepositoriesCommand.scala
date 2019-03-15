package com.diogosantos.github

import scalaz._
import scalaz.Scalaz._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class FetchUserRepositoriesCommand(githubClient: GithubClient) {

  def run(username: String): OptionT[Future, List[RichRepo]] = {
    val eventualRepoNames = githubClient.fetchRepositoriesNames(username)

    eventualRepoNames.flatMap { repoNames: List[String] =>
      repoNames.map { repoName =>
        val eventualContributors =
          githubClient.fetchRepositoryContributors(username, repoName)
        eventualContributors.map(RichRepo(repoName, _))
      }.sequenceU
    }
  }

}
