package di

import play.api.{Application, ApplicationLoader}

class SampleSilhouetteLoader extends ApplicationLoader {
  override def load(context: ApplicationLoader.Context): Application =
    new SilhouetteSampleComponents(context).application
}
