import csstype.*
import emotion.react.css
import io.ktor.http.*
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p

external interface MontruEraroAĵoj: Props {
    var code: HttpStatusCode
    var mesaĝo: String
}

val MontruEraron = FC<MontruEraroAĵoj>{ aĵoj ->
    div{
        css{
            width = 100.pct
            height = 100.pct
            display = Display.flex
            alignContent = AlignContent.center
            justifyContent = JustifyContent.center
            backgroundColor = Color(Colors.secondaryBg)
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
        }
        h1{
            +("${aĵoj.code.value}: ${aĵoj.code.description}")
            css{
                fontSize = 46.px
                color = Color(Colors.primaryFg)
            }
        }
        p{
            +aĵoj.mesaĝo
            css{
                fontSize = 36.px
                color = Color(Colors.secondaryFg)
            }
        }
    }
}