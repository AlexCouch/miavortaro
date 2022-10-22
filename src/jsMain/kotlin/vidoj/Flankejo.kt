package vidoj

import alioj.Colors
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul

external interface SidebarProps: Props {
    var onMenuSelect: (String)->Unit
}

val Sidebar = FC<SidebarProps>{ props ->
    div{
        css(ClassName("flex-container")){
            display = Display.flex
            alignContent = AlignContent.flexStart
            justifyContent = JustifyContent.flexStart
            backgroundColor = Color(Colors.primaryBg)
            width = 15.pct
            marginTop = 3.em
        }
        ul{
            css{
                width = 100.pct
                height = 100.pct
                padding = 0.pct
            }
            li{
                css{
                    listStyle = None.none
                    color = Color(Colors.primaryFg)
                    textAlign = TextAlign.center
                    paddingTop = 1.em
                    paddingBottom = 1.em

                    hover {
                        backgroundColor = Color(Colors.onPrimaryBg)
                        cursor = Cursor.pointer
                    }
                }
                onClick = {
                    props.onMenuSelect("Vortoj")
                }
                p{
                    +"Vortoj"
                }
            }
            li{
                css{
                    listStyle = None.none
                    color = Color(Colors.primaryFg)
                    textAlign = TextAlign.center
                    paddingTop = 1.em
                    paddingBottom = 1.em
                    hover{
                        backgroundColor = Color(Colors.onPrimaryBg)
                        cursor = Cursor.pointer

                    }
                }
                onClick = {
                    props.onMenuSelect("Aretoj")
                }
                p{
                    +"Aretoj"
                }
            }
        }
    }
}