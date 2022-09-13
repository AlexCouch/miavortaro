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
        }
        span{
            css(ClassName("fa fa-plus")){
                color = Color(Colors.secondaryFg)
                marginRight = 2.em
                cursor = Cursor.pointer
            }
            onClick = {
                props.onButtonClick(Buttons.AldoniButono, true)
            }
        }
    }
}