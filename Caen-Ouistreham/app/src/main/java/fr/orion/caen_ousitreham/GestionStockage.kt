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
    private var cstockage: Cstockage? = null
    private var fininavire: Int = 0
    private var finistockage: Int = 0
    private var Navire: Cnavire? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_stockage)

        i = intent.getStringExtra ("reference")
        cstockage = dbHandler?.getOneStockage(i.toString())

        val cnavire = dbHandler?.getSpecificalNavire(cstockage!!.type)
        val listNavire: ArrayList<String> = arrayListOf()


        if (cstockage != null){
            cnavire!!.forEach {
                listNavire.add(it.nLloyds.toString())
            }

            if (listNavire.size > 0){
                picker_listeNavire.minValue = 0
                picker_listeNavire.maxValue = listNavire.size - 1
                picker_listeNavire.displayedValues = listNavire.toTypedArray()
                TextView_TEXT.text = "Transfer cargo from the ${cnavire[picker_listeNavire.value].nomNavire} ship"
                TextView_Error_Message.visibility = View.GONE
            }
            else{
                picker_listeNavire.visibility = View.GONE
            }

            progressbar_capacite.progress = cstockage!!.QuelPourcentage().toInt()
            TextView_POURCENTAGE.text = "${cstockage!!.QuelPourcentage().toInt()}%"

            TextView_NOM_NAVIRE.text = cnavire[picker_listeNavire.value].nomNavire
            TextView_QUANTITEFRET.text = cnavire[picker_listeNavire.value].qteFret.toString()
            TextView_QUANTITE_STORAGE.text = cstockage!!.capaUtil.toString()
            TextView_TEXT.text = "100%"

            when {
                cstockage!!.QuelPourcentage().toInt() <= 50 -> progressbar_capacite.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                cstockage!!.QuelPourcentage().toInt() <= 75 -> progressbar_capacite.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                cstockage!!.QuelPourcentage().toInt() <= 95 -> progressbar_capacite.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                cstockage!!.QuelPourcentage().toInt() <= 100 -> progressbar_capacite.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
            }

            picker_listeNavire.setOnValueChangedListener { picker, oldVal, newVal ->
                TextView_TEXT.text = "Transfer cargo from the ${cnavire[picker.value].nomNavire} ship"
                TextView_NOM_NAVIRE.text = cnavire[picker.value].nomNavire
                TextView_QUANTITEFRET.text = cnavire[picker.value].qteFret.toString()
            }

            Seekbar_POURCENTAGE.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) { TextView_TEXT.text = "$i%" }
            })

            button_DECHARGER.setOnClickListener {

                Navire = cnavire[picker_listeNavire.value]

                var quantiteAjouter = Navire!!.qteFret*Seekbar_POURCENTAGE.progress/100
                val total = quantiteAjouter
                fininavire = Navire!!.qteFret - total
                finistockage = cstockage!!.capaUtil + total

                if (cstockage!!.Stocker(quantiteAjouter) && quantiteAjouter > 0){

                    quantiteAjouter /= 100

                    button_DECHARGER.isClickable = false
                    button_CHARGER.isClickable = false

                    var countDownTimer = object : CountDownTimer(3000, 30) {
                        override fun onTick(millisUntilFinished: Long) {
                            finiTransfert = 11

                            Navire!!.qteFret -= quantiteAjouter
                            TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                            cstockage!!.capaUtil += quantiteAjouter
                            TextView_QUANTITE_STORAGE.text = cstockage!!.capaUtil.toString()

                            progressbar_capacite.progress = cstockage!!.capaUtil*100/cstockage!!.capaDispo
                            when {
                                progressbar_capacite.progress <= 50 -> progressbar_capacite.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                                progressbar_capacite.progress <= 75 -> progressbar_capacite.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                                progressbar_capacite.progress <= 95 -> progressbar_capacite.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                                progressbar_capacite.progress <= 100 -> progressbar_capacite.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
                            }
                            TextView_POURCENTAGE.text = "${progressbar_capacite.progress}%"
                        }
                        override fun onFinish() {
                            Navire!!.qteFret = fininavire
                            cstockage!!.capaUtil = finistockage
                            progressbar_capacite.progress = cstockage!!.capaUtil*100/cstockage!!.capaDispo
                            if (dbHandler!!.updateDataStockage(cstockage!!, cstockage!!) && dbHandler.updateDataNavire(Navire!!)){
                                TextView_TEXT.text = "Transfert reussi"
                                TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                                TextView_QUANTITE_STORAGE.text = cstockage!!.capaUtil.toString()
                                when {
                                    progressbar_capacite.progress <= 50 -> progressbar_capacite.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                                    progressbar_capacite.progress <= 75 -> progressbar_capacite.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                                    progressbar_capacite.progress <= 95 -> progressbar_capacite.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                                    progressbar_capacite.progress <= 100 -> progressbar_capacite.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
                                }
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

                Navire = cnavire[picker_listeNavire.value]

                var quantiteEnMoin = cstockage!!.capaUtil*Seekbar_POURCENTAGE.progress/100
                val total = quantiteEnMoin
                fininavire = Navire!!.qteFret + total
                finistockage = cstockage!!.capaUtil - total

                if (Navire!!.Charger(quantiteEnMoin) && quantiteEnMoin > 0){

                    quantiteEnMoin /= 100

                    button_DECHARGER.isClickable = false
                    button_CHARGER.isClickable = false

                    var countDownTimer = object : CountDownTimer(3000, 30) {
                        override fun onTick(millisUntilFinished: Long) {
                            finiTransfert = 21

                            Navire!!.qteFret += quantiteEnMoin
                            TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                            cstockage!!.capaUtil -= quantiteEnMoin
                            TextView_QUANTITE_STORAGE.text = cstockage!!.capaUtil.toString()

                            progressbar_capacite.progress = cstockage!!.capaUtil*100/cstockage!!.capaDispo
                            when {
                                progressbar_capacite.progress <= 50 -> progressbar_capacite.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                                progressbar_capacite.progress <= 75 -> progressbar_capacite.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                                progressbar_capacite.progress <= 95 -> progressbar_capacite.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                                progressbar_capacite.progress <= 100 -> progressbar_capacite.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
                            }
                            TextView_POURCENTAGE.text = "${progressbar_capacite.progress}%"
                        }
                        override fun onFinish() {
                            Navire!!.qteFret = fininavire
                            cstockage!!.capaUtil = finistockage
                            progressbar_capacite.progress = cstockage!!.capaUtil*100/cstockage!!.capaDispo
                            if (dbHandler!!.updateDataStockage(cstockage!!, cstockage!!) && dbHandler.updateDataNavire(Navire!!)){
                                TextView_TEXT.text = "Transfert reussi"
                                TextView_QUANTITEFRET.text = Navire!!.qteFret.toString()
                                TextView_QUANTITE_STORAGE.text = cstockage!!.capaUtil.toString()
                                when {
                                    progressbar_capacite.progress <= 50 -> progressbar_capacite.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                                    progressbar_capacite.progress <= 75 -> progressbar_capacite.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                                    progressbar_capacite.progress <= 95 -> progressbar_capacite.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                                    progressbar_capacite.progress <= 100 -> progressbar_capacite.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
                                }
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
                cstockage!!.capaUtil = finistockage
                dbHandler!!.updateDataStockage(cstockage!!, cstockage!!)
                dbHandler.updateDataNavire(Navire!!)
            }
            21 -> {
                Navire!!.qteFret = fininavire
                cstockage!!.capaUtil = finistockage
                dbHandler!!.updateDataStockage(cstockage!!, cstockage!!)
                dbHandler.updateDataNavire(Navire!!)
            }
        }
        startActivity(Intent(this, Menu::class.java).apply {})
        finish()
    }


}
