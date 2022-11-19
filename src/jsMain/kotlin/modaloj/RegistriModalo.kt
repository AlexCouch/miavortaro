package modaloj

import utiloj.SerĉBreto
import csstype.Auto
import csstype.Margin
import csstype.pct
import csstype.px
import react.FC
import react.Props
import react.dom.html.ReactHTML.a
import react.useState

external interface RegisterModalProps: Props {
    var show: Boolean
    var jeFermu: ()->Unit
    var Registri: (uzantonomo: String, pasvorto: String) -> Unit
    var malfermuEnsalutadanModalon: ()->Unit
}

val RegisterModal = FC<RegisterModalProps>{ props ->
    val (uzantnomo, metuUzantnomon) = useState<String>("")
    val (pasvorto, metuPasvorton) = useState<String>("")

    Modalo{
        show = props.show
        titolo = "Registri"
        butonoEtikedo = "Registri"
        jeFermu = {
            metuUzantnomon("")
            metuPasvorton("")
            props.jeFermu()
        }
        butonoAlklako = {
            metuUzantnomon("")
            metuPasvorton("")
            props.Registri(uzantnomo, pasvorto)
        }
        SerĉBreto{
            jeŜanĝo = {
                metuUzantnomon(it)
            }
            etikedo = "Uzantnomo"
            larĝo = 50.pct
            margin = Margin(10.px, Auto.auto)
        }
        SerĉBreto{
            jeŜanĝo = {
                metuPasvorton(it)
            }
            etikedo = "Pasvorto"
            larĝo = 50.pct
            margin = Margin(10.px, Auto.auto)
            estasPasvorto = true
        }
        a{
            +"aŭ ensaluti"
            onClick = {
                props.malfermuEnsalutadanModalon()
            }
        }
    }
}