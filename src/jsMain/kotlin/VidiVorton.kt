import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.p
import react.useState

external interface VidiVortonAĵoj: Props {
    var vorto: WordEntry
    var montruVorton: Boolean
    var ŝanĝuVorton: Boolean
    var sendiŜanĝatanVorton: (WordEntry)->Unit
    var jeFermo: ()->Unit
    var malfermuŜanĝejo: (Boolean)->Unit
}

val VidiVorton = FC<VidiVortonAĵoj>{ aĵoj ->
    var (novaVorto, metuNovaVorto) = useState<String>("")
    var (novaPriskribo, metuNovaPriskribo) = useState<String>("")
    div{
        /*
            Aranĝo:
                1. Titolo, aliaj aĵoj
                2. Multaj vidoj de la difinioj de la vortoj, se pli ol unu ekzistas
                3. Vorteroj
                4. Tradukaĵoj
                5. Diskutado
         */
        css{
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.spaceEvenly

            scrollBehavior = ScrollBehavior.smooth
            width = 50.pct
            marginTop = 3.em
            backgroundColor = if(aĵoj.montruVorton) Color(Colors.primaryBg) else Color(Colors.secondaryBg)
            border = if(aĵoj.montruVorton) Border(1.px, LineStyle.solid, NamedColor.black) else None.none
            boxShadow = if(aĵoj.montruVorton) BoxShadow(0.px, 0.px, 1.em, NamedColor.black) else None.none
        }
        if(aĵoj.montruVorton) {
            div{
                css{
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                    margin = Margin(6.px, 16.px)
                }
                MVButono{
                    icon = "fa fa-angle-double-right"
                    jeAlklako = aĵoj.jeFermo
                    fontSize = 1.5.em
                }
                MVButono{
                    icon = "fa fa-edit"
                    jeAlklako = {
                        aĵoj.malfermuŜanĝejo(true)
                    }
                    fontSize = 1.5.em

                }
            }
            div{
                css{
                    display = Display.flex
                    justifyContent = JustifyContent.center
                    flexDirection = FlexDirection.column
                    marginLeft = 2.em
                    marginBottom = Auto.auto
                }
                if(aĵoj.ŝanĝuVorton){
                    label{
                        +"Vorto"
                        css{
                            color = Color(Colors.primaryFg)
                        }
                    }
                    Enmetejo{
                        jeŜanĝo = {
                            metuNovaVorto(it)
                        }
                        larĝo = 50.pct
                        defaŭltaValoro = aĵoj.vorto.word
                        centra = false
                        margin = Margin(8.px, 0.px)
                    }
                }else{
                    h1{
                        +aĵoj.vorto.word
                        css{
                            color = Color(Colors.primaryFg)
                            fontSize = 36.px
                            fontWeight = FontWeight.bold
                        }
                    }
                }
                p{
                    +"priskriboj"
                    css{
                        color = Color(Colors.secondaryFg)
                        fontSize = 14.px
                        borderBottom = Border(1.px, LineStyle.solid, NamedColor.black)
                        margin = 0.px
                    }
                }
                if(aĵoj.ŝanĝuVorton){
                    Enmetejo{
                        jeŜanĝo = {
                            metuNovaPriskribo(it)
                        }
                        larĝo = 50.pct
                        defaŭltaValoro = aĵoj.vorto.definition
                        centra = false
                        margin = Margin(8.px, 0.px)
                    }
                }else {
                    p {
                        +aĵoj.vorto.definition
                        css {
                            color = Color(Colors.primaryFg)
                            fontSize = 24.px
                            textAlign = TextAlign.left
                            marginTop = 0.px
                            marginLeft = 1.em
                        }
                    }
                }
            }
            div{
                css{
                    display = Display.flex
                    justifyContent = JustifyContent.center
                    marginBottom = 1.em
                }
                button{
                    css{
                        backgroundColor = Color(Colors.onPrimaryBg)
                        border = Border(1.px, LineStyle.solid, Color(Colors.highlight))
                        borderRadius = 1.em
                        padding = Padding(4.px, 8.px)
                        hover{
                            boxShadow = BoxShadow(0.`in`, 0.`in`, 1.em, Color(Colors.highlight))
                            cursor = Cursor.pointer
                        }
                    }
                    +"Ŝanĝi"
                    onClick = {
                        aĵoj.sendiŜanĝatanVorton(WordEntry(novaVorto, novaPriskribo))
                        aĵoj.malfermuŜanĝejo(false)
                    }
                }
            }
        }
    }
}