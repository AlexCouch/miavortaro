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
                //Se la rektoro (Principal) ne estas nulo, tiam oni uzu ĝin
                call.principal<JWTPrincipal>()?.let{ principal ->
                    val username = principal.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    //Kontrolu la validecon de la ĵetono
                    expiresAt?.let{
                        if(it <= 0){
                            call.respond(HttpStatusCode.Unauthorized, "Ĵetono malvalidiĝis")
                            return@get
                        }
                    }
                    //Kontrolu se la uzantnomo estas "Admin"
                    if(username != "Admin"){
                        call.respond(HttpStatusCode.Unauthorized, "Uzanto ne rajtigis")
                        return@get
                    }
                    //Alie, respondu OK
                    //TODO: Ni devas respondi kun la adminaj informaĵoj por ke la uzanto povas iel uzi la statistikojn
                    call.respond(HttpStatusCode.OK)
                    return@get
                }
                //Alie, respondu BadRequest
                call.respond(HttpStatusCode.BadRequest, "Konto ne rajtigita")
            }
        }
        /* TODO: Ni devas modifi la POST-an adminan vojon por ke oni povas sendi JSON-an objekton por aldoni aŭ ŝanĝi informaĵojn
           Ekzemple:
            1. Aldoni/Ŝanĝi vortojn, kaj la ĉiuj aliaj informaĵoj de vortoj
            2. Vidi la statistikojn de la interagoj al la servilo (kiom da vidoj de vortoj, ktp)
            3. Krei novajn adminajn kontojn
         */
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