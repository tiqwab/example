package controller

import com.typesafe.scalalogging.LazyLogging
import play.api.mvc._
import play.api.mvc.Results._
import example.Draft1.ContSample._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

class SampleController(cc: ControllerComponents) extends AbstractController(cc) with LazyLogging {
  import SampleController._

  implicit val ec: ExecutionContext = defaultExecutionContext

  // e.g. GET /sample1
  def sample1(): Action[AnyContent] = Action.async { (req: Request[AnyContent]) =>
    Future.successful(Ok("ok"))
  }

  def sample2(): Action[AnyContent] = Action.async { req =>
    Future.successful(Ok("ok"))
  }

  def editIssue(): Action[IssueEditRequest] = Action.async(jsonParser[IssueEditRequest]) { req =>
    val editReq = req.body
    (for {
      conn <- withConnection
      currentIssue <- findIssueById(editReq.id)(conn)
      _ <- isIssueEditable(currentIssue)(BadRequest)
      _ <- updateIssue(editReq.id, editReq.content)
    } yield Ok("ok")).run(Future.successful)
  }

  def jsonParser[A: Reads]: BodyParser[A] = BodyParser("json reader") { request =>
    parse
      .tolerantJson(request)
      .map(_.right.flatMap { jsValue =>
        jsValue.validate(implicitly[Reads[A]]).map(Right.apply).recoverTotal { _ =>
          Left(BadRequest("failed to parse"))
        }
      })
  }

}

object SampleController extends LazyLogging {

  type ActionCont[A] = Cont[Future[Result], A]
  val ActionCont = Cont

  class MyConnection() {
    def open(): Unit = logger.debug("open")
    def commit(): Unit = logger.debug("commit")
    def rollback(): Unit = logger.debug("rollback")
    def close(): Unit = logger.debug("close")
  }

  def is4xx(result: Result): Boolean = result.header.status / 100 == 4
  def is5xx(result: Result): Boolean = result.header.status / 100 == 5

  def withConnection(implicit ec: ExecutionContext): ActionCont[MyConnection] = ActionCont { f =>
    val conn = new MyConnection()
    conn.open()
    try {
      val resFut = f(conn)
      resFut map { res =>
        if (is4xx(res) || is5xx(res)) { conn.rollback(); res } else { conn.commit(); res }
      }
    } finally {
      conn.close()
    }
  }

  case class IssueEditRequest(id: Long, content: String)

  object IssueEditRequest {
    implicit val format: Format[IssueEditRequest] = Json.format[IssueEditRequest]
  }

  case class Issue(id: Long, content: String) {
    def isEditable: Boolean = true
  }

  object Issues {
    private val s: Map[Long, Issue] = Map(1L -> Issue(1L, "hoge"))
    def findById(id: Long): Option[Issue] = s.get(id)
  }

  def findIssueById(id: Long)(implicit conn: MyConnection): ActionCont[Issue] = ActionCont { f =>
    Issues.findById(id) match {
      case None        => Future.successful(NotFound)
      case Some(issue) => f(issue)
    }
  }

  def isIssueEditable(issue: Issue)(result: Result): ActionCont[Unit] =
    if (issue.isEditable) {
      ActionCont.pure(())
    } else {
      ActionCont { _ =>
        Future.successful(result)
      }
    }

  def updateIssue(issueId: Long, content: String): ActionCont[Unit] = ActionCont { f =>
    if (content.contains("500")) Future.successful(InternalServerError)
    else if (content.contains("400")) Future.successful(BadRequest)
    else f(())
  }

}
