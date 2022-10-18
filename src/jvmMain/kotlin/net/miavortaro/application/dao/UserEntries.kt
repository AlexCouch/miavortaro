package net.miavortaro.application.dao

import org.jetbrains.exposed.sql.Table

object UserEntries: Table(){
    val username = varchar("username", 50)
    val passwordHashed = varchar("passwordHashed", 256)
}