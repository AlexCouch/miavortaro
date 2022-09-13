package com.miavortaro.application.dao

import org.jetbrains.exposed.sql.Table

object WordEntries: Table() {
    val word = varchar("word", 20)
    val definition = varchar("definition", 1024)
}