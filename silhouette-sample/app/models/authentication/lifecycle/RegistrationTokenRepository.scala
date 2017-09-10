package models.authentication.lifecycle

import models.Repository
import models.authentication.{RegistrationToken, RegistrationTokenId}

trait RegistrationTokenRepository[Context]
    extends Repository[RegistrationTokenId, RegistrationToken, Context]
