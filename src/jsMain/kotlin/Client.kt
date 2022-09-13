import csstype.pct
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    document.getElementById("root")?.let{ container ->
        document.body?.let{ body ->
            body.style.apply{
                margin = "0"
            }
        }
        createRoot(container).render(App.create())
    }

}