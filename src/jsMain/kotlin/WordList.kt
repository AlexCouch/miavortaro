import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul

external interface WordListProps: Props {
    var words: List<WordEntry>
}

val WordList = FC<WordListProps>{ props ->
    div{
        css {
            backgroundColor = Color(Colors.secondaryBg)
            width = 100.pct
            height = 100.pct - 3.em
            marginTop = 3.em
        }
        if(props.words.isNotEmpty()) {
            ul {
                props.words.forEach {
                    li {
                        css {
                            listStyle = None.none
                            width = 35.pct

                            borderStyle = LineStyle.solid
                            borderColor = NamedColor.transparent
                            borderRadius = 2.px
                            paddingLeft = 1.em
                            borderRadius = 8.px

                            hover {
                                borderColor = Color(Colors.highlight)
                                boxShadow = BoxShadow(0.`in`, 0.`in`, 10.px, Color(Colors.highlight))
                                cursor = Cursor.pointer
                            }
                        }
                        h1 {
                            +it.word
                            css {
                                color = Color(Colors.primaryFg)
                            }
                        }
                        p {
                            +(if (it.definition.length > 100) it.definition.slice(0..100) else it.definition)
                            css {
                                color = Color(Colors.primaryFg)
                            }
                        }
                    }
                }
            }
        }else{
            p{
                +"Vorto(j) ne trovita(j)"
                css{
                    color = Color(Colors.secondaryFg)
                    fontSize = 24.px
                    textAlign = TextAlign.center
                }
            }
        }
    }
}