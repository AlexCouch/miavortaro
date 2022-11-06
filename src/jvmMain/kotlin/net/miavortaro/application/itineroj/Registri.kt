@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.miavortaro.application.itineroj

import User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Routing
import net.miavortaro.application.dao

@Location("/registri")
class Registri

fun Routing.registry(){
    post<Registri>{
        val user = call.receive<User>()
        if(dao.queryUser(user.username)){
            call.respond(HttpStatusCode.BadRequest, "Uzantnomo jam tenita")
            return@post
        }
        dao.createUser(user)
        call.respondRedirect("/")
    }
}