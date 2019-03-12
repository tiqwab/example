package di

import com.softwaremill.macwire._
import controller.SampleController
import play.api.mvc.ControllerComponents

trait SampleComponents {
  def controllerComponents: ControllerComponents

  lazy val sampleController: SampleController =
    wire[SampleController]
}
