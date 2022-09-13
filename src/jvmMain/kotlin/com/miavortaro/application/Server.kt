@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.miavortaro.application

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.miavortaro.application.dao.DAOFacade
import com.miavortaro.application.dao.DAOFacadeCache
import com.miavortaro.application.dao.DAOFacadeDatabase
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.partialcontent.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import io.netty.handler.codec.DefaultHeaders
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database
import java.io.File
import java.sql.Driver
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toByteArray

val dir = File("build/db")

val pool = ComboPooledDataSource().apply{
    driverClass = Driver::class.java.name
    jdbcUrl = "jdbc:h2:file:${dir.canonicalFile.absolutePath}"
    user = ""
    password = ""
}

val hashKey = hex("6819b57a326945c1968f45236589")
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

val dao: DAOFacade = DAOFacadeCache(DAOFacadeDatabase(Database.connect(pool)), File(dir.parentFile, "ehcache"))

@Location("/")
class Index

fun Application.main(){
    dao.init()

    environment.monitor.subscribe(ApplicationStopped){ pool.close() }

    mainWithDependencies(dao)
}

fun Application.mainWithDependencies(dao: DAOFacade){
    install(DefaultHeaders)
    install(Locations)
    install(CallLogging)
    install(ConditionalHeaders)
    install(PartialContent)
    install(Sessions){
        //TODO: Add account sessions
    }
    install(ContentNegotiation){
        json()
    }

    val hashFunction = { s: String -> hash(s) }

    routing{
        index()
        static("/static"){
            resources()
        }
    }
}

fun hash(password: String): String =
    Mac.getInstance("HmacSHA1").let {
        it.init(hmacKey)
        hex(it.doFinal(password.toByteArray(Charsets.UTF_8)))
    }