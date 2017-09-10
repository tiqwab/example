package controllers

import com.mohiva.play.silhouette.api.actions.{
  SecuredErrorHandler => SilhouetteSecuredErrorHandler
}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

class SecuredErrorHandler extends SilhouetteSecuredErrorHandler {

  override def onNotAuthenticated(
      implicit request: RequestHeader): Future[Result] = {
    Future.successful(Unauthorized)
  }

  override def onNotAuthorized(
      implicit request: RequestHeader): Future[Result] = {
    Future.successful(Forbidden)
  }

}
