package di

import com.softwaremill.macwire._
import play.api.routing.Router
import play.api._
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import play.filters.cors.{CORSComponents, CORSFilter}
import router.Routes

// ref. https://www.playframework.com/documentation/2.6.x/ScalaCompileTimeDependencyInjection
class MyApplicationLoader extends ApplicationLoader {
  override def load(context: ApplicationLoader.Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }

    new MyComponents(context).application
  }
}

class MyComponents(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context) with HttpFiltersComponents with CORSComponents with HomeComponents
  with SampleComponents {

  override def httpFilters: Seq[EssentialFilter] =
    super.httpFilters ++ Seq(corsFilter)

  override def router: Router = {
    val prefix: String = "/"
    wire[Routes]
  }
}
