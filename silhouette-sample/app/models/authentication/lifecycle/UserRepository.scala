package models.authentication.lifecycle

import models.Repository
import models.authentication.{User, UserId}

trait UserRepository[Context] extends Repository[UserId, User, Context]
