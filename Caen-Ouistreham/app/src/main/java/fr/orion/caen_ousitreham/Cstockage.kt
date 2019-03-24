package fr.orion.caen_ousitreham

import android.annotation.SuppressLint

@Suppress("UNREACHABLE_CODE")
class Cstockage (
    var reference: String,
    var type: String, // type= « cuve ou zone(entrepôt) ou silo »
    var capaDispo: Int,// Entier : capacité de stockage disponible
    var capaUtil: Int,// Entier : capacité utilisé
    var unite: String,// unité de la capacité de   stockage (cuve : litres, zone : nb de containers, silo : m3)
    var img: Int
) {

    // member function
    @SuppressLint("SetTextI18n")
    fun Affiche(): String {
        return "Référence : $reference\n" +
                "Type de stockage : $type\n" +
                "Capacité disponible : $capaDispo\n" +
                "Capacité utilisé : $capaUtil\n" +
                "Unité : $unite\n"
                "${QuelPourcentage()}"
    }


    fun Stocker(quantite_en_plus: Int): Boolean// stocke la quantité qté et met à jour la capacité disponible
    {
        return capaUtil + quantite_en_plus <= capaDispo
    }
    fun Vider(quantite_en_moin: Int): Boolean// stocke la quantité qté et met à jour la capacité disponible
    {
        return capaUtil - quantite_en_moin >= 0
    }
    fun EstVide(): Boolean// indique si la zone de stockage est vide ou non
    {
        return this.capaUtil == 0
    }

    fun EstRemplie(): Boolean // Booléen // indique si la zone de stockage est remplie ou non
    {
        return this.capaDispo == this.capaUtil
    }

    fun PrecisionQuantite(pourcentage: Int): Int{
        return this.capaUtil * pourcentage/100
    }

    fun QuelPourcentage(): Double // donne le pourcentage de remplissage et permet de mettre à jour capaDispo ... }
    {
        return Math.round(capaUtil.toDouble() * 100.0) / capaDispo.toDouble()
    }
}