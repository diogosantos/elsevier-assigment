package com.diogosantos.github
import cats.free.Free
import github4s.{GHRepos, GithubResponses}
import github4s.GithubResponses.{GHIO, GHResponse, GHResult}
import github4s.free.domain.Repository
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import scalaz.OptionT

import scala.concurrent.Future

class GithubClientSpec extends FlatSpec with MockitoSugar with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  val githubRepos: GHRepos = mock[GHRepos]

  val githubClient = new GithubClient(githubRepos)

  val username = "diogo"

  "githubClient" should "fetch repository names from Github API for a given username" in {
//    val repos = List[Repository](mockRepo("My Repo 1"), mockRepo("My Repo 2"))
//    val repositories = ???
//    given(githubRepos.listUserRepos(username)).willReturn(repositories)
//
//    val repoNames: OptionT[Future, List[String]] =
//      githubClient.fetchRepositoriesNames(username)
//
//    Mockito.verify(githubRepos).listUserRepos(username)
//    ScalaFutures.whenReady(repoNames.run) { repoNames =>
//      val expected = repos.map(_.name)
//      repoNames should equal(expected)
//    }
  }

  def mockRepo(name: String): Repository = {
    val repo1 = mock[Repository]
    given(repo1.name).willReturn(name)
    repo1
  }

}
