package fr.orion.caen_ouistreham

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.orion.caen_ousitreham.Cstockage
import fr.orion.caen_ousitreham.DatabaseHandler
import fr.orion.caen_ousitreham.Menu
import fr.orion.caen_ousitreham.R
import kotlinx.android.synthetic.main.activity_add_stockage.*

class AddStockage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stockage)
        val dbHandler: DatabaseHandler?
        //init db
        dbHandler = DatabaseHandler(this)

        val listType: Array<String> = arrayOf("Conteneur", "Silo", "Liquide")
        val listeN: Array<String> = arrayOf("Numero")
        val listeL: Array<String> = arrayOf("m^3")

        picker_stockage_type.minValue = 0
        picker_stockage_type.maxValue = listType.size - 1
        picker_stockage_type.displayedValues = listType
        picker_stockage_type.value = 1

        picker_stockage_unite.minValue = 0
        picker_stockage_unite.maxValue = listeL.size - 1
        picker_stockage_unite.displayedValues = listeL
        picker_stockage_unite.value = 0

        picker_stockage_type.setOnValueChangedListener { picker, oldVal, newVal ->
            when (picker.value){
                0 ->{
                    picker_stockage_unite.minValue = 0
                    picker_stockage_unite.maxValue = 0
                    picker_stockage_unite.displayedValues = listeN
                    picker_stockage_unite.value = 0
                }
                1 -> {
                    picker_stockage_unite.minValue = 0
                    picker_stockage_unite.maxValue = 0
                    picker_stockage_unite.displayedValues = listeL
                    picker_stockage_unite.value = 0
                }
            }
        }



        //on Click Save button
        button_save_stockage.setOnClickListener {
            // checking input text should not be null
            if (validation()) {
                val valeurpickers_type: String = when (picker_stockage_type.value){
                    0 -> "Conteneur"
                    1 -> "Silo"
                    2 -> "Liquide"
                    else -> {
                        "Silo"
                    }
                }
                val valeurpickes_unite: String = when (picker_stockage_unite.value){
                    0 -> "Numero"
                    1 -> "m^3"
                    else -> {
                        "m^3"
                    }
                }
                val stockage = Cstockage(
                    editText_Reference.text.toString(),
                    valeurpickers_type,
                    editText_capacitémax.text.toString().toInt(),
                    0,
                    valeurpickes_unite,
                    0
                )
                val success: Boolean = dbHandler.addStockage(stockage)
                if (success) {
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, AddStockage::class.java).apply {})
                    finish()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validation(): Boolean {
        return if (editText_Reference.text.toString() != "" && editText_capacitémax.text.toString() != "") {
            true
        } else {
            Toast.makeText(this, "All fields must be completed", Toast.LENGTH_LONG).show()
            false
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, Menu::class.java).apply {})
        finish()
    }
}