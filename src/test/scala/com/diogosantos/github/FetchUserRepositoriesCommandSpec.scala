package com.diogosantos.github
import org.mockito.ArgumentMatchers.{any => many, eq => meq}
import org.mockito.BDDMockito.given
import org.mockito.Mockito.{reset, verify, verifyNoMoreInteractions}
import org.mockito.internal.verification.Times
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import scalaz._

import scala.concurrent.Future

class FetchUserRepositoriesCommandSpec
    extends FlatSpec
    with MockitoSugar
    with Matchers
    with BeforeAndAfter {

  import scala.concurrent.ExecutionContext.Implicits.global

  val githubClient: GithubClient = mock[GithubClient]

  val fetchUserRepositoriesCommand = new FetchUserRepositoriesCommand(
    githubClient
  )

  before {
    reset(githubClient)
  }

  val username = "diogo"

  "fetchUserRepositoriesCommand" should "list rich repositories for a given user name" in {
    val repoNames = List("project1", "project2")
    given(githubClient.fetchRepositoriesNames(username))
      .willReturn(liftList(repoNames))

    val contribs = List("con1", "con2")
    given(
      githubClient.fetchRepositoryContributors(many[String](), many[String]())
    ).willReturn(liftList(contribs))

    val richRepositories = fetchUserRepositoriesCommand.run(username)

    verify(githubClient).fetchRepositoriesNames(meq(username))
    verify(githubClient, new Times(2))
      .fetchRepositoryContributors(many[String](), many[String]())

    ScalaFutures.whenReady(richRepositories.run) { result =>
      val expected = repoNames.map(RichRepo(_, contribs))
      result should equal(Some(expected))

    }
  }

  it should "list nothing if fetching repositories fail" in {
    val failed =
      OptionT(Future.failed[Option[List[String]]](new RuntimeException()))
    given(githubClient.fetchRepositoriesNames(username)).willReturn(failed)

    val richRepositories = fetchUserRepositoriesCommand.run(username)

    verify(githubClient).fetchRepositoriesNames(meq(username))
    verifyNoMoreInteractions(githubClient)

    ScalaFutures.whenReady(richRepositories.run.failed) { e =>
      e shouldBe a[RuntimeException]
    }
  }

  it should "list nothing if fetching contributors fail" in {
    val repoNames = List("project1", "project2")
    given(githubClient.fetchRepositoriesNames(username))
      .willReturn(liftList(repoNames))

    val failed =
      OptionT(Future.failed[Option[List[String]]](new RuntimeException()))
    given(
      githubClient.fetchRepositoryContributors(many[String](), many[String]())
    ).willReturn(failed)

    val richRepositories = fetchUserRepositoriesCommand.run(username)

    verify(githubClient).fetchRepositoriesNames(meq(username))
    verify(githubClient, new Times(2))
      .fetchRepositoryContributors(many[String](), many[String]())

    ScalaFutures.whenReady(richRepositories.run.failed) { e =>
      e shouldBe a[RuntimeException]
    }
  }

  private def liftList(list: List[String]): OptionT[Future, List[String]] =
    OptionT(Future(Option(list)))

}
