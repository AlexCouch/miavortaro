package utiloj

import alioj.Colors
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input

external interface SerĉBretaAĵoj: Props{
    var jeŜanĝo: (String)->Unit
    var larĝo: Length
    var etikedo: String
    var margin: Margin
    var centra: Boolean
    var defaŭltaValoro: String
    var estasPasvorto: Boolean
}

val SerĉBreto = FC<SerĉBretaAĵoj>{ aĵoj ->
    form{
        css(ClassName("flex-container")){
            display = Display.flex
            justifyContent = if(aĵoj.centra) JustifyContent.center else JustifyContent.normal
            alignContent = if(aĵoj.centra) AlignContent.center else AlignContent.normal
            width = 100.pct
        }

        onSubmit = {
            it.preventDefault()
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
            type = if(aĵoj.estasPasvorto) InputType.password else InputType.text

        }
    }
}