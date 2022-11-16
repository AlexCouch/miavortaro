package vidoj

import alioj.Colors
import WordEntry
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
    var onWordClick: (WordEntry) -> Unit
    var onScroll: ()->Unit
}

val WordList = FC<WordListProps>{ props ->
    div{
        css {
            backgroundColor = Color(Colors.secondaryBg)
            width = 50.pct
            height = 100.pct - 3.em
            marginTop = 3.em
            overflow = Overflow.scroll
//            marginRight = 5.pct
        }
        if(props.words.isNotEmpty()) {
            onScroll = {
                it.currentTarget.let{ target ->
                    if(target.scrollHeight - target.scrollTop == target.clientHeight.toDouble()){
                        props.onScroll()
                    }
                }
            }
            ul {
                props.words.forEach { word ->
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
                            +word.word
                            css {
                                color = Color(Colors.primaryFg)
                            }
                        }
                        p {
                            +(if (word.definition.length > 100) word.definition.slice(0..100) else word.definition)
                            css {
                                color = Color(Colors.primaryFg)
                            }
                        }
                        onClick = {
                            props.onWordClick(word)
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