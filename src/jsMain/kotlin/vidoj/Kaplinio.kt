package vidoj

import alioj.Buttons
import alioj.Colors
import csstype.*
import csstype.LineStyle.Companion.solid
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul
import react.useState
import utiloj.Enmetejo
import utiloj.MVButono

external interface HeaderProps: Props{
    var malfermiMenuon: Boolean
    var onButtonClick: (String, Boolean) -> Unit
    var onSearchUpdate: (String)->Unit
    var onLogin: (String)->Unit
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
            etikedo = "Serĉi per tajpi esperante..."
            centra = true
        }
        /*MVButono{
            icon = "fa fa-plus"
            fontSize = 1.2.em
            jeAlklako = {
                props.onButtonClick(Buttons.AldoniButono, true)
                props.onButtonClick(Buttons.MalfermiMenuon, false)

            }
            margin = Margin(top = 0.px, bottom = 0.px, right = 1.em, left = 0.px)
        }
        div{
            css{
                display = Display.flex
                flexDirection = FlexDirection.column
            }
            MVButono{
                icon = "fa fa-bars"
                fontSize = 1.2.em
                jeAlklako = {
                    props.onButtonClick(Buttons.MalfermiMenuon, !props.malfermiMenuon)
                }
                margin = Margin(top = 0.px, bottom = 0.px, right = 1.em, left = 0.px)
            }
            div{
                css{
                    display = if(props.malfermiMenuon) Display.block else None.none
                    backgroundColor = Color(Colors.primaryBg)
                    color = Color(Colors.secondaryFg)
                    border = Border(1.px, LineStyle.solid, NamedColor.black)
                    boxShadow = BoxShadow(5.px, 10.px, 18.px, rgba(10, 10, 10, 0.5))
                    position = Position.absolute
                    marginTop = 2.15.em
                    marginLeft = (-9).em
                }
                ul{
                    css{
                        margin = 0.px
                        padding = 0.px
                    }
                    li{
                        i{
                            css(ClassName("fa-solid fa-right-to-bracket")){}
                        }
                        +" Ensaluti"
                        css{
                            backgroundColor = Color(Colors.secondaryBg)
                            color = Color(Colors.secondaryFg)
                            listStyle = None.none
                            padding = 1.em

                            hover{
                                backgroundColor = Color(Colors.onPrimaryBg)
                            }
                        }
                        onClick = {
                            props.onButtonClick(Buttons.Ensaluti, true)
                            props.onButtonClick(Buttons.MalfermiMenuon, false)
                        }
                    }
                    li{
                        i{
                            css(ClassName("fa-solid fa-wallet")){}
                        }
                        +" Subteni"
                        css{
                            backgroundColor = Color(Colors.secondaryBg)
                            color = Color(Colors.secondaryFg)
                            listStyle = None.none
                            padding = 1.em
                            hover{
                                backgroundColor = Color(Colors.onPrimaryBg)
                            }
                        }
                        //TODO: Aldoni modalon por subteni
                    }
                    li{
                        i{
                            css(ClassName("fa-solid fa-book")){
                            }
                        }
                        +" Programa Dokumentaro"
                        css{
                            backgroundColor = Color(Colors.secondaryBg)
                            color = Color(Colors.secondaryFg)
                            listStyle = None.none
                            padding = 1.em

                            hover{
                                backgroundColor = Color(Colors.onPrimaryBg)
                            }
                        }
                        //TODO: Aldoni modalon por legi la programan dokumentaron
                    }
                }
            }
        }*/
    }
}