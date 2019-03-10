package di

import com.softwaremill.macwire._
import controller.HomeController
import play.api.mvc.ControllerComponents

trait HomeComponents {
  def controllerComponents: ControllerComponents

  lazy val homeController: HomeController =
    wire[HomeController]
}
