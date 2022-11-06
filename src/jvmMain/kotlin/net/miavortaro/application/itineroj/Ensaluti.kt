@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.miavortaro.application.itineroj

import User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Routing
import net.miavortaro.application.createToken
import net.miavortaro.application.dao
import javax.crypto.SecretKey

@Location("/ensaluti")
class Ensaluti

fun Routing.ensaluti(audience: String, issuer: String, secret: String){
    post<Ensaluti>{
        val user = call.receive<User>()
        if(!dao.authUser(user)){
            call.respond(HttpStatusCode.Forbidden, "Uzanto ne aŭtentikigita")
            return@post
        }
        val token = createToken(audience, issuer, user, secret)
        call.respond(mapOf("token" to token))
    }
}