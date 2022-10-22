package utiloj

import alioj.Colors
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.i

external interface MVButonoAĵoj: Props{
    var icon: String
    var fontSize: FontSize
    var margin: Margin
    var jeAlklako: ()->Unit
}

val MVButono = FC<MVButonoAĵoj>{ aĵoj ->
    i{
        css(ClassName(aĵoj.icon)){
            cursor = Cursor.pointer
            color = Color(Colors.onPrimaryBg)
            fontSize = aĵoj.fontSize
            margin = aĵoj.margin
            hover{
                color = Color(Colors.buttonHover)
            }
        }
        onClick = {
            aĵoj.jeAlklako()
        }
    }
}