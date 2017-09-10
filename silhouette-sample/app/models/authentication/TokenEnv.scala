package models.authentication

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.BearerTokenAuthenticator

trait TokenEnv extends Env {
  type I = User
  type A = BearerTokenAuthenticator
}
