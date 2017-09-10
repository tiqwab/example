package di

import com.mohiva.play.silhouette.api.actions._
import com.mohiva.play.silhouette.api.{
  Environment,
  EventBus,
  Silhouette,
  SilhouetteProvider
}
import com.mohiva.play.silhouette.api.repositories.{
  AuthInfoRepository,
  AuthenticatorRepository
}
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.impl.authenticators.{
  BearerTokenAuthenticator,
  BearerTokenAuthenticatorService,
  BearerTokenAuthenticatorSettings
}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.util.{
  DefaultFingerprintGenerator,
  PlayCacheLayer,
  SecureRandomIDGenerator
}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.persistence.repositories.{
  CacheAuthenticatorRepository,
  DelegableAuthInfoRepository
}
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import com.softwaremill.macwire._
import com.softwaremill.tagging._
import controllers.{SecuredErrorHandler => MySecuredErrorHandler, _}
import models.authentication.TokenEnv
import models.authentication.dao.PasswordInfoDAO
import models.{ContextCreator, JDBCContextCreator}
import models.authentication.lifecycle.{
  LoginInfoRepository,
  LoginInfoRepositoryOnJdbc,
  PasswordInfoRepository,
  PasswordInfoRepositoryOnJdbc,
  RegistrationTokenRepository,
  RegistrationTokenRepositoryOnJdbc,
  UserRepository,
  UserRepositoryOnJdbc
}
import models.authentication.service.{LoginService, UserService}
import play.api.cache.{CacheApi, EhCacheComponents}
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.api.i18n.{I18nComponents, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import router.Routes
import scalikejdbc.DBSession

class SilhouetteSampleComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with I18nComponents
    with DBComponents
    with HikariCPComponents
    with EhCacheComponents
    with EvolutionsComponents
    with SecuredActionComponents
    with UnsecuredActionComponents
    with UserAwareActionComponents
    with UnsecuredErrorHandlerComponents {

  override lazy val dynamicEvolutions: DynamicEvolutions =
    new DynamicEvolutions

  val scalikePlayInitializer = wire[scalikejdbc.PlayInitializer]

  lazy val contextCreator: ContextCreator[DBSession] = JDBCContextCreator

  lazy val secureSampleController = wire[SecureSampleController]

  lazy val securedErrorHandler = new MySecuredErrorHandler()

  // --- silhouette ---

  /* Implementation for silhouette */
  lazy val userRepository: UserRepository[DBSession] =
    new UserRepositoryOnJdbc("USERS")
  lazy val userService: UserService =
    wire[UserService]
  lazy val loginService: LoginService =
    wire[LoginService]
  lazy val loginInfoRepository: LoginInfoRepository[DBSession] =
    new LoginInfoRepositoryOnJdbc("LOGIN_INFO")
  lazy val registrationTokenRepository: RegistrationTokenRepository[DBSession] =
    new RegistrationTokenRepositoryOnJdbc("REGISTRATION_TOKENS")
  lazy val passwordInfoRepository: PasswordInfoRepository[DBSession] =
    new PasswordInfoRepositoryOnJdbc("PASSWORD_INFO")
  lazy val delegatableAuthInfoDao: DelegableAuthInfoDAO[PasswordInfo] =
    wire[PasswordInfoDAO]

  /* Classes coming from silhouette */
  lazy val cacheLayer: CacheLayer = new PlayCacheLayer(defaultCacheApi)
  lazy val idGenerator: IDGenerator = new SecureRandomIDGenerator()
  lazy val passwordHasher: PasswordHasher = new BCryptPasswordHasher()
  lazy val fingerprintGenerator: FingerprintGenerator =
    new DefaultFingerprintGenerator(false)
  lazy val eventBus = EventBus()
  lazy val authInfoRepository: AuthInfoRepository =
    new DelegableAuthInfoRepository(delegatableAuthInfoDao)
  lazy val credentialsProvider: CredentialsProvider =
    new CredentialsProvider(authInfoRepository,
                            PasswordHasherRegistry(passwordHasher))
  lazy val authenticatorService
    : AuthenticatorService[BearerTokenAuthenticator] = {
    val authenticatorDAO =
      new CacheAuthenticatorRepository[BearerTokenAuthenticator](cacheLayer)
    val settings = BearerTokenAuthenticatorSettings()
    new BearerTokenAuthenticatorService(settings,
                                        authenticatorDAO,
                                        idGenerator,
                                        Clock())
  }

  lazy val sEnvironment: Environment[TokenEnv] = Environment[TokenEnv](
    userService,
    authenticatorService,
    Seq(),
    eventBus
  )

  lazy val silhouette: Silhouette[TokenEnv] =
    wire[SilhouetteProvider[TokenEnv]]

  // Environment と Provider が必要とするのが考えやすい?

  // --- silhouette ---

  override lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }
  lazy val assets: Assets = wire[Assets]

  // eval lazy applicationEvolutions to start evolutions
  applicationEvolutions

}
