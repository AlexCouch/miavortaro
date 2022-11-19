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

external interface LoginModalProps: Props {
    var show: Boolean
    var jeFermu: ()->Unit
    var Ensaluti: (uzantonomo: String, pasvorto: String) -> Unit
    var malfermuRegistradanModalon: ()->Unit
}

val LoginModal = FC<LoginModalProps>{ props ->
    val (uzantnomo, metuUzantnomon) = useState<String>("")
    val (pasvorto, metuPasvorton) = useState<String>("")

    Modalo{
        show = props.show
        titolo = "Ensaluti"
        butonoEtikedo = "Ensaluti"
        jeFermu = {
            metuUzantnomon("")
            metuPasvorton("")
            props.jeFermu()
        }
        butonoAlklako = {
            props.Ensaluti(uzantnomo, pasvorto)
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
            +"aŭ registriĝi"
            onClick = {
                props.malfermuRegistradanModalon()
            }
        }
    }
}