package com.diogosantos.github

import github4s.GHRepos
import github4s.Github._
import github4s.GithubResponses.GHResponse
import github4s.free.domain.Repository
import github4s.jvm.Implicits._
import scalaj.http.HttpResponse

import scala.concurrent.Future

class GithubClient(githubRepos: GHRepos) {

  import scala.concurrent.ExecutionContext.Implicits.global

  def fetchRepositoriesNames(
    username: String
  ): Future[Either[GitHubError, List[String]]] = {
    val listUserRepos = githubRepos.listUserRepos(username)
    val eventualResponse: Future[GHResponse[List[Repository]]] =
      listUserRepos.execFuture[HttpResponse[String]]()
    for (response <- eventualResponse)
      yield
        response match {
          case Right(result) => Right(result.result.map(_.name))
          case Left(e)       => Left(GitHubError(e))
        }
  }

  def fetchRepositoryContributors(
    username: String,
    repoName: String
  ): Future[Either[GitHubError, List[String]]] = {
    val listContributors = githubRepos.listContributors(username, repoName)
    val eventualResponse = listContributors.execFuture[HttpResponse[String]]()
    for (response <- eventualResponse)
      yield
        response match {
          case Right(result) => Right(result.result.map(_.login))
          case Left(e)       => Left(GitHubError(e))
        }
  }

}

case class GitHubError(e: Throwable) {
  def message: String = e.getMessage
}
