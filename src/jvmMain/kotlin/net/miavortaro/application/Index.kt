@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.miavortaro.application

import WordEntry
import net.miavortaro.application.dao.DAOFacade
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.locations.*
import io.ktor.server.locations.delete
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun HTML.index(){
    head{
        link(rel = "stylesheet", href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css")
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
                            "'vortoj' devas havi valaron, sed ĝi ne ja! Vidu la dokumentaĵon de la API por pli!"
                        )
                        else -> {
                            call.respond(dao.searchWords(it["vortoj"]!!).map { word -> dao.getWord(word) }
                                .map { word -> WordEntry(word.word, word.description) })
                        }
                    }
                }
                else if("ĉio" in it){
                    if(it["ĉio"] == "vera"){
                        call.respond(dao.all().map { entry -> dao.getWord(entry) }.map { word -> WordEntry(word.word, word.description) })
                    }else{
                        call.respondHtml {
                            index()
                        }
                    }
                } else{
                    call.respond(HttpStatusCode.BadRequest, "Radiko devas havi valarojn en la 'vortoj' enmeto!")
                }
            }
        }
    }
    post<Index>{
        call.receive<WordEntry>().let {
            dao.createWord(it.word, it.definition)
        }
        call.respond(HttpStatusCode.OK)
    }
    delete<Index> {
        call.receive<String>().let {
            dao.deleteWord(it)
        }
        call.respond(HttpStatusCode.OK)
    }
}