package com.diogosantos.github

import github4s.Github._
import github4s.GithubResponses.GHResponse
import github4s.free.domain.Repository
import github4s.jvm.Implicits._
import github4s.{GHRepos, GithubResponses}
import scalaj.http.HttpResponse
import scalaz._

import scala.concurrent.Future

class GithubClient(githubRepos: GHRepos) {

  import scala.concurrent.ExecutionContext.Implicits.global

  def fetchRepositoriesNames(
    username: String
  ): OptionT[Future, List[String]] = {
    val listUserRepos = githubRepos.listUserRepos(username)
    val eventualResponse: Future[GHResponse[List[Repository]]] =
      listUserRepos.execFuture[HttpResponse[String]]()

    liftT(eventualResponse) { repos =>
      repos.map(_.name)
    }
  }

  def fetchRepositoryContributors(
    username: String,
    repoName: String
  ): OptionT[Future, List[String]] = {
    val listContributors = githubRepos.listContributors(username, repoName)
    val eventualResponse = listContributors.execFuture[HttpResponse[String]]()

    liftT(eventualResponse) { contribs =>
      contribs.map(_.login)
    }
  }

  private def liftT[A](
    eventualResponse: Future[GithubResponses.GHResponse[A]]
  )(f: A => List[String]): OptionT[Future, List[String]] = {
    val eventualOption = eventualResponse.map {
      case Right(v) => Some(f(v.result))
      case Left(_)  =>
        //TODO: log the error
        None
    }
    OptionT(eventualOption)
  }

}
