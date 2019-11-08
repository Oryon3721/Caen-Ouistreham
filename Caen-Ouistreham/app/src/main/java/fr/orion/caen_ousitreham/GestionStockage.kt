package fr.orion.caen_ousitreham

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_gestion_stockage.*
import android.os.CountDownTimer
import android.widget.SeekBar


class GestionStockage : AppCompatActivity() {

    private var i: String? = null
    private var finiTransfert: Int = 10
    private val dbHandler: DatabaseHandler? = DatabaseHandler(this)
    private var storage: Cstockage? = null
    private var fininavire: Int = 0
    private var finistockage: Int = 0
    private var Navire: Cnavire? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_stockage)

        i = intent.getStringExtra ("reference")
        storage = dbHandler?.getOneStockage(i.toString())

        val listShip = dbHandler?.getSpecificalNavire(storage!!.type)
        val listNavirenLloyds: ArrayList<String> = arrayListOf()


        if (storage != null){
            listShip!!.forEach {
                listNavirenLloyds.add(it.nLloyds.toString())
            }

            if (listNavirenLloyds.size > 0){
                picker_listeNavire.minValue = 0
                picker_listeNavire.maxValue = listNavirenLloyds.size - 1
                picker_listeNavire.displayedValues = listNavirenLloyds.toTypedArray()
                TextView_TEXT.text = "Transfer cargo from the ${listShip[picker_listeNavire.value].nomNavire} ship"
                TextView_Error_Message.visibility = View.GONE
            }
            else{
                picker_listeNavire.visibility = View.GONE
            }

            progressbar_capacite.progress = storage!!.QuelPourcentage().toInt()
            TextView_POURCENTAGE.text = "${storage!!.QuelPourcentage().toInt()}%"

            TextView_NOM_NAVIRE.text = listShip[picker_listeNavire.value].nomNavire
            TextView_QUANTITEFRET.text = listShip[picker_listeNavire.value].qteFret.toString()
            TextView_QUANTITE_STORAGE.text = storage!!.capaUtil.toString()
            TextView_TEXT.text = "100%"

            progressionBar()

            picker_listeNavire.setOnValueChangedListener { picker, oldVal, newVal ->
                TextView_TEXT.text = "Transfer cargo from the ${listShip[picker.value].nomNavire} ship"
                TextView_NOM_NAVIRE.text = listShip[picker.value].nomNavire
                TextView_QUANTITEFRET.text = listShip[picker.value].qteFret.toString()
            }

            Seekbar_POURCENTAGE.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) { TextView_TEXT.text = "$i%" }
            })

            button_DECHARGER.setOnClickListener {

                Navire = listShip[picker_listeNavire.value]

                var quantiteAjouter = Navire!!.qteFret*Seekbar_POURCENTAGE.progress/100
                val total = quantiteAjouter
                fininavire = Navire!!.qteFret - total
                finistockage = storage!!.capaUtil + total

                if (storage!!.Stocker(quantiteAjouter) && quantiteAjouter > 0){

                    quantiteAjouter /= 100

                    button_DECHARGER.isClickable = false
                    button_CHARGER.isClickable = false

                    var countDownTimer = object : CountDownTimer(3000, 30) {
                        override fun onTick(millisUntilFinished: Long) {
                            finiTransfert = 11

                            Navire!!.qteFret -= quantiteAjouter
                            TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                            storage!!.capaUtil += quantiteAjouter
                            TextView_QUANTITE_STORAGE.text = storage!!.capaUtil.toString()

                            progressbar_capacite.progress = storage!!.capaUtil*100/storage!!.capaDispo
                            progressionBar()
                            TextView_POURCENTAGE.text = "${progressbar_capacite.progress}%"
                        }
                        override fun onFinish() {
                            Navire!!.qteFret = fininavire
                            storage!!.capaUtil = finistockage
                            progressbar_capacite.progress = storage!!.capaUtil*100/storage!!.capaDispo
                            if (dbHandler!!.updateDataStockage(storage!!, storage!!) && dbHandler.updateDataNavire(Navire!!)){
                                TextView_TEXT.text = "Transfert reussi"
                                TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                                TextView_QUANTITE_STORAGE.text = storage!!.capaUtil.toString()
                                progressionBar()
                                TextView_POURCENTAGE.text = "${progressbar_capacite.progress}%"
                                finiTransfert = 10
                            }
                            button_DECHARGER.isClickable = true
                            button_CHARGER.isClickable = true
                        }
                    }
                    countDownTimer.start()
                }
                else{

                }
            }

            button_CHARGER.setOnClickListener {

                Navire = listShip[picker_listeNavire.value]

                var quantiteEnMoin = storage!!.capaUtil*Seekbar_POURCENTAGE.progress/100
                val total = quantiteEnMoin
                fininavire = Navire!!.qteFret + total
                finistockage = storage!!.capaUtil - total

                if (Navire!!.Charger(quantiteEnMoin) && quantiteEnMoin > 0){

                    quantiteEnMoin /= 100

                    button_DECHARGER.isClickable = false
                    button_CHARGER.isClickable = false

                    var countDownTimer = object : CountDownTimer(3000, 30) {
                        override fun onTick(millisUntilFinished: Long) {
                            finiTransfert = 21

                            Navire!!.qteFret += quantiteEnMoin
                            TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                            storage!!.capaUtil -= quantiteEnMoin
                            TextView_QUANTITE_STORAGE.text = storage!!.capaUtil.toString()

                            progressbar_capacite.progress = storage!!.capaUtil*100/storage!!.capaDispo
                            progressionBar()
                            TextView_POURCENTAGE.text = "${progressbar_capacite.progress}%"
                        }
                        override fun onFinish() {
                            Navire!!.qteFret = fininavire
                            storage!!.capaUtil = finistockage
                            progressbar_capacite.progress = storage!!.capaUtil*100/storage!!.capaDispo
                            if (dbHandler!!.updateDataStockage(storage!!, storage!!) && dbHandler.updateDataNavire(Navire!!)){
                                TextView_TEXT.text = "Transfert reussi"
                                TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                                TextView_QUANTITE_STORAGE.text = storage!!.capaUtil.toString()
                                progressionBar()
                                TextView_POURCENTAGE.text = "${progressbar_capacite.progress}%"
                                finiTransfert = 10
                            }
                            button_DECHARGER.isClickable = true
                            button_CHARGER.isClickable = true
                        }
                    }
                    countDownTimer.start()
                }
                else{

                }
            }
        }
    }


    override fun onBackPressed() {
        when (finiTransfert) {
            11 -> {
                Navire!!.qteFret = fininavire
                storage!!.capaUtil = finistockage
                dbHandler!!.updateDataStockage(storage!!, storage!!)
                dbHandler.updateDataNavire(Navire!!)
            }
            21 -> {
                Navire!!.qteFret = fininavire
                storage!!.capaUtil = finistockage
                dbHandler!!.updateDataStockage(storage!!, storage!!)
                dbHandler.updateDataNavire(Navire!!)
            }
        }
        startActivity(Intent(this, Menu::class.java).apply {})
        finish()
    }

    fun progressionBar(){
        when {
            progressbar_capacite.progress <= 50 -> progressbar_capacite.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
            progressbar_capacite.progress <= 75 -> progressbar_capacite.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
            progressbar_capacite.progress <= 95 -> progressbar_capacite.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
            progressbar_capacite.progress <= 100 -> progressbar_capacite.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}
