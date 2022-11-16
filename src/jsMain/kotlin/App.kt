import alioj.Buttons
import csstype.*
import emotion.react.css
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import modaloj.AldoniVorton
import modaloj.LoginModal
import modaloj.RegisterModal
import react.*
import react.dom.html.ReactHTML.div
import vidoj.*

val httpClient = HttpClient{
    install(ContentNegotiation){
        json()
    }
}

val mainScope = MainScope()

sealed class AppState{
    object Load: AppState()
    object Vortoj: AppState()
    object Aretoj: AppState()
    data class Error(val code: HttpStatusCode, val message: String): AppState()
}

suspend fun searchWord(word: String, setWords: StateSetter<List<WordEntry>>){
    httpClient.get("/"){
        parameter("vortoj", word)
    }.let { response ->
        val body = response.body<List<WordEntry>>()
        setWords(body)
    }
}

suspend fun getAllWords(komenco: Int, fino: Int, updateWords: (List<WordEntry>)->Unit): Boolean{
    httpClient.get("/"){
        parameter("tranĉi", "$komenco,$fino")
    }.let { response ->
        if(response.bodyAsText() == "fine"){
            return false
        }
        val body = response.body<List<WordEntry>>()
        updateWords(body)
        return true
    }
}

suspend fun addOrUpdateWord(word: WordEntry, updateAppState: StateSetter<AppState>, updateWords: (List<WordEntry>)->Unit): Boolean{
    httpClient.post("/"){
        headers{
            contentType(ContentType.Application.Json)
        }
        setBody(word)
    }.let { response ->
        if (response.status != HttpStatusCode.OK) {
            updateAppState(AppState.Error(response.status, "Ho ve! Io okazis! Pardonu min! Vi povas reprovi poste!"))
        } else {
            return getAllWords(0, 10, updateWords)
        }
    }
    return true
}

//NOTE: This will return the token to be stored in a cookie
suspend fun ensaluti(user: User, updateAppState: StateSetter<AppState>): String?{
    httpClient.post("/ensaluti"){
        headers{
            contentType(ContentType.Application.Json)
        }
        setBody(user)
    }.let { response ->
        return if (response.status != HttpStatusCode.OK) {
            updateAppState(AppState.Error(response.status, response.body()))
            null
        } else {
            val tokenData = response.body<Map<String, String>>()
            tokenData["token"]
        }
    }
}

suspend fun registri(user: User, updateAppState: StateSetter<AppState>): Boolean{
    httpClient.post("/registri"){
        headers{
            contentType(ContentType.Application.Json)
        }
        setBody(user)
    }.let { response ->
        return if(response.status != HttpStatusCode.OK){
            updateAppState(AppState.Error(response.status, response.status.description))
            false
        }else{
            true
        }
    }
}

val App = FC<Props>{
    val (appState, updateAppState) = useState<AppState>(AppState.Load)
    val (words, updateWords) = useState<List<WordEntry>>(emptyList())
    val (showAddWordModal, shouldShowAddWordModal) = useState<Boolean>(false)
    val (showLoginModal, shouldShowLoginModal) = useState<Boolean>(false)
    val (showRegisterModal, shouldShowRegisterModal) = useState<Boolean>(false)
    val (malfermiMenuon, jaMalfermiMenuon) = useState<Boolean>(false)
    val (showWord, shouldShowWord) = useState<Boolean>(false)
    val (wordToShow, setWordToShow) = useState<WordEntry>()
    val (changeWord, setChangeWord) = useState<Boolean>(false)
    val (scrollWordListStart, setScrollWordListStart) = useState<Int>(0)
    val (scrollWordListEnd, setScrollWordListEnd) = useState<Int>(10)
    val (fineDeLaListo, setFineDeLaListo) = useState<Boolean>(false)

    div {
        css(ClassName("flex-container")) {
            display = Display.flex
            position = Position.fixed
            width = 100.pct
            height = 100.pct
            margin = 0.pct
        }
        AldoniVorton {
            show = showAddWordModal
            jeFermu = {
                shouldShowAddWordModal(false)
            }
            AldoniVortonFunc = { vorto, priskribo ->
                mainScope.launch {
                    addOrUpdateWord(WordEntry(vorto, priskribo), updateAppState){
                        setFineDeLaListo(false)
                        updateWords(it)
                    }
                }
            }
        }
        LoginModal{
            show = showLoginModal
            jeFermu = {
                shouldShowLoginModal(false)
            }
            Ensaluti = { uzantnomo, pasvorto ->
                mainScope.launch{
                    ensaluti(User(uzantnomo, pasvorto), updateAppState).let{ token ->
                        //TODO:
                        //  1. Validigi ĵetonon ja estas ne-nulo
                        //  2. Stoki validigitan ĵetonon en kuketo esti reuzota
                        //  3. Ĝisdatigi la staton de la apo por esti Ensalutita kaj montri la kontajn informojn en la kaplinio
                        println(
                            if(token != null){
                                println("Token = $token")
                            }else{
                                println("Could not get a token.")
                            }
                        )
                    }
                }
            }
            malfermuRegistradanModalon = {
                shouldShowLoginModal(false)
                shouldShowRegisterModal(true)
            }
        }
        RegisterModal{
            show = showRegisterModal
            jeFermu = {
                shouldShowRegisterModal(false)
            }
            Registri = { uzantnomo, pasvorto ->
                mainScope.launch{
                    registri(User(uzantnomo, pasvorto), updateAppState).let{ result ->
                        //TODO:
                        //  1. Kontrolu la rezulton de la peto ĉu vera aŭ malvera
                        //  2. Se vera, montru al la uzanto ke si povas ensaluti, aliflanke, montru eraron
                        println(
                            "Rezulto de la registrado: $result"
                        )
                    }
                }
            }
            malfermuEnsalutadanModalon = {
                shouldShowRegisterModal(false)
                shouldShowLoginModal(true)
            }
        }
        Header {
            this.malfermiMenuon = malfermiMenuon
            onButtonClick = { button, toggle ->
                when (button) {
                    Buttons.AldoniButono -> {
                        shouldShowAddWordModal(toggle)
                        shouldShowLoginModal(false)
                    }
                    Buttons.MalfermiMenuon -> {
                        jaMalfermiMenuon(toggle)
                        shouldShowAddWordModal(false)
                    }
                }
            }
            onSearchUpdate = { word ->
                mainScope.launch {
                    if (word.isEmpty()) {
                        getAllWords(0, 10){
                            setFineDeLaListo(false)
                            updateWords(it)
                        }
                    } else {
                        searchWord(word, updateWords)
                    }
                }
            }
        }
        Sidebar {
            onMenuSelect = {
                when (it) {
                    "Vortoj" -> {
                        shouldShowWord(false)
                        setChangeWord(false)
                        println("Trying to close the view word window")
                        updateAppState(AppState.Vortoj)
                        mainScope.launch {
                            getAllWords(scrollWordListStart, scrollWordListEnd){
                                setFineDeLaListo(false)
                                updateWords(it)
                            }
                        }
                    }
                }
            }
        }
        when (appState) {
            is AppState.Load -> {
                mainScope.launch {
                    getAllWords(scrollWordListStart, scrollWordListEnd){
                        setFineDeLaListo(false)
                        updateWords(it)
                    }
                    setScrollWordListStart(scrollWordListStart + 10)
                    setScrollWordListEnd(scrollWordListEnd + 10)
                    updateAppState(AppState.Vortoj)
                }
            }
            is AppState.Vortoj -> {
                WordList {
                    this.words = words
                    onWordClick = {
                        setWordToShow(it)
                        shouldShowWord(true)
                    }
                    onScroll = {
                        if(!fineDeLaListo){
                            setScrollWordListStart(scrollWordListStart + 10)
                            setScrollWordListEnd(scrollWordListEnd + 10)
                            mainScope.launch{
                                val rezulto = getAllWords(scrollWordListStart, scrollWordListEnd){
                                    updateWords(words + it)
                                }
                                setFineDeLaListo(rezulto)
                            }
                        }
                    }
                }
                ViewWord {
                    wordToShow?.let {
                        println(it)
                        vorto = it
                    }
                    this.changeWord = changeWord
                    this.showWord = showWord
                    onClose = {
                        shouldShowWord(false)
                        setChangeWord(false)
                    }
                    closeChangePlace = {
                        setChangeWord(it)
                    }
                    sendChangedWord = { word ->
                        mainScope.launch {
                            addOrUpdateWord(word, updateAppState){
                                updateWords(it)
                            }
                            setWordToShow(word)
                            setChangeWord(false)
                        }
                    }
                }
            }
            is AppState.Aretoj -> Unit
            is AppState.Error -> {
                MontruEraron {
                    code = appState.code
                    mesaĝo = appState.message
                }
            }
        }

    }
}