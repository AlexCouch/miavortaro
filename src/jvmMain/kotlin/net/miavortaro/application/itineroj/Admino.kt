@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.miavortaro.application.itineroj

import User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Routing
import net.miavortaro.application.dao
import net.miavortaro.application.verifyAdminToken
import net.miavortaro.application.verifyHTTPS

@Location("/admin")
class Admino

fun Routing.admin(){
    authenticate("auth-jwt") {
        get<Admino>{
            if(call.verifyHTTPS()){
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Saluton, $username! La ĵetono eksvalidiĝas post $expiresAt ms.")
            }
        }
        post<Admino>{
            if(call.verifyHTTPS()) {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val password = principal.payload.getClaim("hashedPassword").asString()

                val expiresAt = principal.expiresAt!!.time.minus(System.currentTimeMillis())
                if (username != "admin") {
                    call.respond("Nur admino povas atingi ĉi tiun itineron")
                    return@post
                }
                if (expiresAt > 30 * 60 * 1000) {
                    call.respond("Ĵetono eksvalidiĝis")
                    return@post
                }
                if (!dao.authUser(User(username, password))) {
                    call.respond("Admino ne plu aŭtentikigita")
                    return@post
                }
                call.respond(dao.allUsers())
            }
        }
        delete<Admino>{
            if(call.verifyHTTPS()){
                val principal = call.principal<JWTPrincipal>() ?: return@delete //NOTE: Ĉi tiu neniam okazos pro la Token plugin supre
                if(!principal.verifyAdminToken()){
                    call.respond(HttpStatusCode.Forbidden, "Nur la admino povas trafi ĉi tiun itineron")
                    return@delete
                }
                val userToDelete = call.receive<String>()
                if(!dao.queryUser(userToDelete)){
                    call.respond(HttpStatusCode.BadRequest, "Uzanto devas esti valida")
                    return@delete
                }
                if(!dao.deleteUserByName(userToDelete)){
                    call.respond(HttpStatusCode.BadRequest, "Ne eblis forigi uzanton ial /shrug")
                    return@delete
                }
            }
        }
    }
}