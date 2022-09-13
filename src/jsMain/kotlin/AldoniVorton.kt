import csstype.*
import csstype.Display.Companion.flex
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import react.FC
import react.Props
import react.StateSetter
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.html
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState

external interface AldoniVortonProps: Props {
    var show: Boolean
    var jeFermu: () -> Unit
    var AldoniVortonFunc: (vorto: String, priparolo: String) -> Unit
}

val AldoniVorton = FC<AldoniVortonProps>{ props ->
    val (vorto, ĝisdatigiVorton) = useState<String>("")
    val (priskribo, ĝisdatigiPriskribon) = useState<String>("")

    div{
        //Ĉi tiu enhavas vicojn kiuj enhavas la partojn de la tuta formo.
        //Vico unua: La 'eks' marko
        //Vico dua: La enmetejo por la vorto
        //Vico tria: La priskribo de la vorto
        //Vico kvara: La butono por sendi la vorton al la servo
        id = "aldoniVorton"
        css{
            display = if(props.show) Display.block else None.none
            position = Position.fixed
            zIndex = integer(1)
            left = 0.em
            top = 0.em
            width = 100.pct
            height = 100.pct
            overflow = Auto.auto
            backgroundColor = rgba(3,3,3,0.3)
        }
        div{
            css(ClassName("flex-container")){
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.spaceBetween
                alignContent = AlignContent.spaceBetween

                backgroundColor = Color(Colors.primaryBg)
                margin = Margin(5.pct, Auto.auto)
                border = Border(1.px, LineStyle.solid, Color(Colors.onPrimaryBg))
                borderRadius = 1.em
                width = 35.pct
                height = 75.pct
            }
            div{
                //Jen la vico unua, en kiu ekzistas la 'eks' marko por fermi la formon
                css(ClassName("flex-container")){
                    width = 100.pct
                    display = Display.flex
                    placeContent = PlaceContent.flexEnd
                }
                i{
                    css(ClassName("fa fa-close")){
                        color = Color(Colors.onPrimaryBg)
                        marginRight = 8.px
                        marginTop = 8.px
                        cursor = Cursor.pointer
                        float = Float.right
                        width = 1.em
                        height = 1.em
                    }
                    onClick = {
                        props.jeFermu()
                    }
                }
            }
            div{
                //Jen la vico dua, en kiu estas la enmetejo por la vorto
                css{
                    width = 100.pct
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    justifyContent = JustifyContent.center
                    alignItems = AlignItems.center
                }
                Enmetejo{
                    jeŜanĝo = {
                        ĝisdatigiVorton(it)
                    }
                    etikedo = "Vorto"
                    larĝo = 50.pct
                    margin = Margin(10.px, Auto.auto)
                }
                Enmetejo{
                    jeŜanĝo = {
                        ĝisdatigiPriskribon(it)
                    }
                    etikedo = "Priskribi"
                    larĝo = 50.pct
                }
            }
            div{
                css(ClassName("flex-container")){
                    display = Display.flex
                    justifyContent = JustifyContent.center
                }
                button{
                    css{
                        display = Display.flex
                        justifyContent = JustifyContent.center
                        backgroundColor = Color(Colors.onPrimaryBg)
                        border = Border(1.px, LineStyle.solid, Color(Colors.highlight))
                        borderRadius = 1.em
                        hover{

                        }
                    }
                    +"Aldoni"

                    onClick = {
                        props.AldoniVortonFunc(vorto, priskribo)
                    }
                }
            }
        }
    }
}