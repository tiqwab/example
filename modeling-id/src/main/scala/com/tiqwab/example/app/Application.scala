package com.tiqwab.example.app

import com.tiqwab.example.modeling.Entity

case class Application(
    id: ApplicationId,
    name: String
) extends Entity[ApplicationId]
