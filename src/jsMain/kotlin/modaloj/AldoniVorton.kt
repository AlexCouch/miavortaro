package modaloj

import utiloj.SerĉBreto
import csstype.*
import react.FC
import react.Props
import react.useState

external interface AldoniVortonProps: Props {
    var show: Boolean
    var jeFermu: ()->Unit
    var AldoniVortonFunc: (vorto: String, priparolo: String) -> Unit
}

val AldoniVorton = FC<AldoniVortonProps>{ props ->
    val (vorto, ĝisdatigiVorton) = useState<String>("")
    val (priskribo, ĝisdatigiPriskribon) = useState<String>("")

    Modalo{
        show = props.show
        titolo = "Aldoni"
        butonoEtikedo = "Aldoni"
        jeFermu = {
            ĝisdatigiVorton("")
            ĝisdatigiPriskribon("")
            props.jeFermu()
        }
        butonoAlklako = {
            ĝisdatigiVorton("")
            ĝisdatigiPriskribon("")
            props.AldoniVortonFunc(vorto, priskribo)
        }
        SerĉBreto{
            jeŜanĝo = {
                ĝisdatigiVorton(it)
            }
            etikedo = "Vorto"
            larĝo = 50.pct
            margin = Margin(10.px, Auto.auto)
        }
        SerĉBreto{
            jeŜanĝo = {
                ĝisdatigiPriskribon(it)
            }
            etikedo = "Priskribo"
            larĝo = 50.pct
            margin = Margin(10.px, Auto.auto)
        }
    }
}