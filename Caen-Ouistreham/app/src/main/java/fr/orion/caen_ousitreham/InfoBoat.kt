package fr.orion.caen_ousitreham

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_info_boat.*

class InfoBoat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_boat)

        val i: Int? = intent.getIntExtra ("NLoyds", 0)

        val dbHandler: DatabaseHandler? = DatabaseHandler(this)
        val cnavire = dbHandler?.getOneNavire(i!!.toInt())

        if (cnavire != null){
            NLLOYDS.setText(cnavire.nLloyds.toString())
            NAME_NAV.setText(cnavire.nomNavire)
            LIBELLE_FRET.setText(cnavire.libelleFret)
            QTE_FRET.setText(cnavire.qteFret.toString())
            CAPAMAX.setText(cnavire.capaMaxN.toString())
        }

        val list: Array<String> = arrayOf("Conteneur", "Graine", "Liquide")

        picker_change.minValue = 0
        picker_change.maxValue = list.size - 1

        picker_change.displayedValues = list

        when (cnavire!!.typeFret){
            "Conteneur" -> picker_change.value = 0
            "Graine" -> picker_change.value = 1
            "Liquide" -> picker_change.value = 2
            else -> {
                picker_change.value = 1
            }
        }

        button_save_boat_change.setOnClickListener{
            // checking input text should not be null
            if (validation()){
                val valeurpickers: String = when (picker_change.value){
                    0 -> "Conteneur"
                    1 -> "Graine"
                    2 -> "Liquide"
                    else -> {
                        "Graine"
                    }
                }
                val bateau = Cnavire(NLLOYDS.text.toString().toInt(), NAME_NAV.text.toString(), LIBELLE_FRET.text.toString(), valeurpickers, QTE_FRET.text.toString().toInt(), CAPAMAX.text.toString().toInt(), 0)
                val success: Boolean = dbHandler.updateDataNavire(bateau)
                if (success){
                    Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, ListBoat::class.java).apply {})
                    finish()
                }
                else{
                    Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                }
            }

        }

        button_delete_boat_change.setOnClickListener {
            if (dbHandler.deleteDataNavire(cnavire.nLloyds)){
                Toast.makeText(this,"Delete Successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ListBoat::class.java).apply {})
                finish()
            }
            else{
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validation(): Boolean{
        return if (NLLOYDS.text.toString() != "" && NAME_NAV.text.toString()!= "" && LIBELLE_FRET.text.toString()!= "" && QTE_FRET.text.toString() != "" && CAPAMAX.text.toString() != "" && QTE_FRET.text.toString().toInt() <= CAPAMAX.text.toString().toInt()){
            true
        } else {
            Toast.makeText(this,"All fields must be completed", Toast.LENGTH_LONG).show()
            false
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, ListBoat::class.java).apply {})
        finish()
    }
}
