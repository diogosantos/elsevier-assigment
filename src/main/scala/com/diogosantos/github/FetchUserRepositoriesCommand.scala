package com.diogosantos.github

import scalaz.Scalaz._
import scalaz._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FetchUserRepositoriesCommand(githubClient: GithubClient) {

  def run(username: String): OptionT[Future, List[RichRepo]] = {
    for {
      repoNames <- githubClient.fetchRepositoriesNames(username)
      richRepos <- repoNames.map { repoName =>
        githubClient
          .fetchRepositoryContributors(username, repoName)
          .map(RichRepo(repoName, _))
      }.sequenceU
    } yield richRepos
  }

}
