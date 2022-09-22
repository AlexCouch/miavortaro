import csstype.*
import emotion.css.css
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input

external interface Enmetejaĵoj: Props{
    var jeŜanĝo: (String)->Unit
    var larĝo: Length
    var etikedo: String
    var margin: Margin
    var centra: Boolean
    var defaŭltaValoro: String
}

val Enmetejo = FC<Enmetejaĵoj>{ aĵoj ->
    form{
        css(ClassName("flex-container")){
            display = Display.flex
            justifyContent = if(aĵoj.centra) JustifyContent.center else JustifyContent.normal
            alignContent = if(aĵoj.centra) AlignContent.center else AlignContent.normal
            width = 100.pct
        }
        input{
            css{
                width = aĵoj.larĝo
                border = Border(4.px, LineStyle.solid, Color(Colors.highlight))
                borderRadius = 1.em
                margin = aĵoj.margin
                backgroundColor = Color(Colors.secondaryBg)
                paddingLeft = 8.px
                color = Color(Colors.secondaryFg)
                placeholder {
                    color = Color(Colors.secondaryFg)
                }
            }
            onChange = {
                aĵoj.jeŜanĝo(it.currentTarget.value)
            }
            value = aĵoj.defaŭltaValoro
            placeholder = aĵoj.etikedo
            type = InputType.text
        }
    }
}