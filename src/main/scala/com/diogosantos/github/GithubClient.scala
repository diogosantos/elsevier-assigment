package com.diogosantos.github

import com.typesafe.scalalogging.Logger
import github4s.Github._
import github4s.GithubResponses.GHResponse
import github4s.free.domain.Repository
import github4s.jvm.Implicits._
import github4s.{GHRepos, GithubResponses}
import scalaj.http.HttpResponse
import scalaz._

import scala.concurrent.{ExecutionContext, Future}

class GithubClient(githubRepos: GHRepos)(
  implicit executionContext: ExecutionContext
) {

  val logger = Logger(getClass)

  def fetchRepositoriesNames(
    username: String
  ): OptionT[Future, List[String]] = {
    val listUserRepos = githubRepos.listUserRepos(username)
    val eventualResponse: Future[GHResponse[List[Repository]]] =
      listUserRepos.execFuture[HttpResponse[String]]()

    liftOptionT(eventualResponse) { repos =>
      repos.map(_.name)
    }
  }

  def fetchRepositoryContributors(
    username: String,
    repoName: String
  ): OptionT[Future, List[String]] = {
    val listContributors = githubRepos.listContributors(username, repoName)
    val eventualResponse = listContributors.execFuture[HttpResponse[String]]()

    liftOptionT(eventualResponse) { contribs =>
      contribs.map(_.login)
    }
  }

  private def liftOptionT[A, B](
    eventualResponse: Future[GithubResponses.GHResponse[A]]
  )(f: A => B): OptionT[Future, B] = {
    val eventualOption = eventualResponse.map {
      case Right(v) => Some(f(v.result))
      case Left(e)  =>
        logger.error("Error fetching data from Github: ", e)
        None
    }
    OptionT(eventualOption)
  }

}
