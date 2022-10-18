@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.miavortaro.application

import User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mchange.v2.c3p0.ComboPooledDataSource
import net.miavortaro.application.dao.DAOFacade
import net.miavortaro.application.dao.DAOFacadeCache
import net.miavortaro.application.dao.DAOFacadeDatabase
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import io.netty.handler.codec.DefaultHeaders
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database
import java.io.File
import java.sql.Date
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

fun createToken(audience: String, issuer: String, user: User, secret: String): String =
    JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", user.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(secret))

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
    val secret = environment.config.property("jwt.secret").toString()
    val issuer = environment.config.property("jwt.issuer").toString()
    val audience = environment.config.property("jwt.audience").toString()
    val myRealm = environment.config.property("jwt.realm").toString()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication){
        jwt("auth-jwt"){
            realm = myRealm

            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
            )

            validate { credential ->
                credential.payload.getClaim("username")?.asString()?.let { username ->
                    if(username != "" && dao.queryUser(username)){
                        JWTPrincipal(credential.payload)
                    }else{
                        null
                    }
                }
            }
        }
    }

    routing{
        index()
        post("/registri"){
            val user = call.receive<User>()
            if(dao.queryUser(user.username)){
                call.respond(HttpStatusCode.BadRequest, "Uzantnomo jam tenita")
                return@post
            }
            dao.createUser(user)
            call.respondRedirect("/")
        }
        post("/ensaluti"){
            val user = call.receive<User>()
            if(!dao.authUser(user)){
                call.respond(HttpStatusCode.Forbidden, "Uzanto ne aŭtentikigita")
                return@post
            }
            val token = createToken(audience, issuer, user, secret)
            call.respond(mapOf("token" to token))
        }
        authenticate("auth-jwt") {
            get("/admin"){
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Saluton, $username! La ĵetono eksvalidiĝas post $expiresAt ms.")
            }
        }
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