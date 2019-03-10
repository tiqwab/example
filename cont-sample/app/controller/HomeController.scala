package controller

import play.api.mvc._

import scala.concurrent.Future

class HomeController(cc: ControllerComponents) extends AbstractController(cc) {

  def index(): Action[AnyContent] = Action.async { implicit req =>
    Future.successful(Ok("ok"))
  }

}
