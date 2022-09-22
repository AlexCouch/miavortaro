import csstype.*
import csstype.LineStyle.Companion.solid
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.useState

external interface HeaderProps: Props{
    var onButtonClick: (String, Boolean) -> Unit
    var onSearchUpdate: (String)->Unit
}

val Header = FC<HeaderProps>{ props ->
    val (search, updateSearch) = useState<String>("")
    div{
        className = ClassName("flex-container")
        css{
            display = Display.flex
            alignContent = AlignContent.center
            alignItems = AlignItems.center
            justifyContent = JustifyContent.start
            flexDirection = FlexDirection.row
            width = 100.pct
            backgroundColor = Color(Colors.primaryBg)
            position = Position.absolute
            height = 3.em
            borderBottomColor = Color(Colors.secondaryBg)
            borderBottomWidth = 1.px
            borderBottomStyle = solid
        }

        Enmetejo{
            jeŜanĝo = {
                props.onSearchUpdate(it)
            }
            larĝo = 50.pct
            etikedo = "Serĉi..."
            centra = true

        }
        MVButono{
            icon = "fa fa-plus"
            fontSize = 1.em
            jeAlklako = {
                props.onButtonClick(Buttons.AldoniButono, true)
            }
            margin = Margin(top = 0.px, bottom = 0.px, right = 1.em, left = 0.px)
        }
    }
}