package modaloj

import alioj.Colors
import csstype.*
import emotion.react.css
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span

external interface DialogoProps: PropsWithChildren {
    var show: Boolean
    var jeFermu: () -> Unit
    var titolo: String
    var butonoEtikedo: String
    var butonoAlklako: ()->Unit
    var korpo: ()->Unit
}

val Modalo = FC<DialogoProps>{ props ->
    div{
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
                //Jen la kaplinio de la dialogo
                css(ClassName("flex-container")){
                    width = 100.pct
                    display = Display.flex
                    placeContent = PlaceContent.spaceBetween
                }
                span{}
                p{
                    +props.titolo
                    css{
                        color = Color(Colors.primaryFg)
                        fontSize = 24.px
                    }
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
                        hover{
                            color = Color(Colors.buttonHover)
                        }
                    }
                    onClick = {
                        props.jeFermu()
                    }
                }
            }
            div{
                //Jen la korpo de la dialogo
                css{
                    width = 100.pct
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    justifyContent = JustifyContent.center
                    alignItems = AlignItems.center
                }
                +props.children
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
                        marginBottom = 10.px
                        hover{
                            boxShadow = BoxShadow(0.`in`, 0.`in`, 1.em, Color(Colors.highlight))
                        }
                    }
                    +props.butonoEtikedo

                    onClick = {
                        props.butonoAlklako()
                        props.jeFermu()
                    }
                }
            }
        }
    }
}