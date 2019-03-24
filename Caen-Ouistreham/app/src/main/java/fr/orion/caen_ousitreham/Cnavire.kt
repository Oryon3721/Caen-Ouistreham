package fr.orion.caen_ousitreham

class Cnavire(
    var nLloyds: Int,
    var nomNavire: String,
    var libelleFret: String,
    var typeFret: String,
    var qteFret: Int,
    var capaMaxN: Int,
    var img: Int
) {

    fun Decharger(quantite_en_moin: Int): Boolean// Diminue la quantité courante de qté
    {
        return qteFret - quantite_en_moin >= 0
    }
    fun Charger(quantite_en_plus: Int): Boolean// Diminue la quantité courante de qté
    {
        return qteFret + quantite_en_plus <= capaMaxN
    }

    fun PrecisionQuantiteFret(pourcentage: Int): Int{
        return (this.qteFret * pourcentage/100)
    }

    fun EstDecharge(): Boolean {//Booléen indique si la quantité courante est null ou non ...
        return this.qteFret != 0
    }
}