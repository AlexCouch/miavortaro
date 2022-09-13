import csstype.*
import emotion.react.css
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.js.TimeStamp
import kotlinx.js.timers.setInterval
import react.*
import react.dom.html.ReactHTML.div
import kotlin.js.Date
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds

val httpClient = HttpClient{
    install(ContentNegotiation){
        json()
    }
}

val mainScope = MainScope()

enum class AppState{
    Load, Vortoj, Aretoj
}

suspend fun searchWord(word: String, setWords: StateSetter<List<WordEntry>>){
    httpClient.get("/"){
        parameter("vortoj", word)
    }.let { response ->
        val body = response.body<List<WordEntry>>()
        setWords(body)
    }
}

suspend fun getAllWords(setWords: StateSetter<List<WordEntry>>){
    httpClient.get("/"){
        parameter("ĉio", "vera")
    }.let { response ->
        val body = response.body<List<WordEntry>>()
        setWords(body)
    }
}

val App = FC<Props>{
    val (appState, updateAppState) = useState<AppState>(AppState.Load)
    val (words, updateWords) = useState<List<WordEntry>>(emptyList())
    val (showAddWordModal, shouldShowAddWordModal) = useState<Boolean>(false)

    div{
        css(ClassName("flex-container")){
            display = Display.flex
            position = Position.fixed
            width = 100.pct
            height = 100.pct
            margin = 0.pct
        }
        AldoniVorton{
            show = showAddWordModal
            jeFermu = {
                shouldShowAddWordModal(false)
            }
            AldoniVortonFunc = { vorto, priparolo ->
                //TODO: Aldonu la vorto al la servo
                println("Aldonita! $vorto -> $priparolo")
            }
        }
        Header{
            onButtonClick = { button, toggle ->
                when(button){
                    Buttons.AldoniButono -> shouldShowAddWordModal(toggle)
                }
            }
            onSearchUpdate = { word ->
                mainScope.launch {
                    if(word.isEmpty()){
                        getAllWords(updateWords)
                    }else{
                        searchWord(word, updateWords)
                    }
                }
            }
        }
        Sidebar{
            onMenuSelect = {
                when(it){
                    "Vortoj" -> {
                        mainScope.launch {
                            getAllWords(updateWords)
                        }
                    }
                }
            }
        }
        when(appState){
            AppState.Load -> {
                mainScope.launch {
                    getAllWords(updateWords)
                    updateAppState(AppState.Vortoj)
                }
            }
            AppState.Vortoj -> {
                WordList{
                    this.words = words
                }
            }
            AppState.Aretoj -> Unit
        }
    }
}