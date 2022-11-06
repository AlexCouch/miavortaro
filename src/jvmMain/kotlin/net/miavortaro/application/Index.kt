@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.miavortaro.application

import WordEntry
import net.miavortaro.application.dao.DAOFacade
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.locations.*
import io.ktor.server.locations.delete
import io.ktor.server.locations.post
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun HTML.index(){
    head{
        link(rel = "stylesheet", href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/fontawesome.min.css")
    }
    body{
        div{
            id = "root"
            script(src = "/static/miavortaro.js") {}
        }
    }
}

fun Routing.index(){
    get<Index>{
        if(call.parameters.isEmpty()){
            call.respondHtml {
                index()
            }
        }else {
            call.parameters.let {
                if ("vortoj" in it) {
                    when (it["vortoj"]) {
                        null -> call.respond(
                            HttpStatusCode.BadRequest,
                            "'vortoj' devas havi valoron, sed ĝi ne ja! Vidu la dokumentaĵon de la API por pliaj detaloj!"
                        )
                        else -> {
                            call.respond(dao.searchWords(it["vortoj"]!!).map { word -> dao.getWord(word) }
                                .map { word -> WordEntry(word.word, word.description) })
                        }
                    }
                }
                else if("listo" in it){
                    if(it["listo"]?.all { char -> char.isDigit() } != null){
                        val listoGrando = it["listo"]?.toInt() ?: 10
                        val listo = dao.top(listoGrando).map { eniro -> dao.getWord(eniro) }.map { vorto -> WordEntry(vorto.word, vorto.description) }
                        call.respond(listo)
                    }else{
                        call.respond(HttpStatusCode.BadRequest, "Atendis kvanton da vortoj per kiuj respondi")
                    }
                } else{
                    call.respondHtml {
                        index()
                    }
                }
            }
        }
    }
    authenticate("auth-jwt"){
        post<Index>{
            if(call.request.local.scheme != "https"){
                call.respond(HttpStatusCode.BadRequest, "Vi bezonas uzi https kaj ne http")
            }
            call.receive<WordEntry>().let {
                dao.createWord(it.word, it.definition)
            }
            call.respond(HttpStatusCode.OK)
        }
        delete<Index> {
            if(call.request.local.scheme != "https"){
                call.respond(HttpStatusCode.BadRequest, "Vi bezonas uzi https kaj ne http")
            }
            call.receive<String>().let {
                dao.deleteWord(it)
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}