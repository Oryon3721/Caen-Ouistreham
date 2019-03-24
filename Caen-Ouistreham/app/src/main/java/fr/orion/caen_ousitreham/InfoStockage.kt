package fr.orion.caen_ousitreham

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_info_stockage.*

class InfoStockage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_stockage)
        val i: String? = intent.getStringExtra ("reference")

        val dbHandler: DatabaseHandler? = DatabaseHandler(this)
        val cstockage = dbHandler?.getOneStockage(i.toString())

        val listType: Array<String> = arrayOf("Conteneur", "Silo", "Liquide")
        val listeN: Array<String> = arrayOf("Numero")
        val listeL: Array<String> = arrayOf("m^3")

        picker_stockage_TYPE.minValue = 0
        picker_stockage_TYPE.maxValue = listType.size - 1
        picker_stockage_TYPE.displayedValues = listType
        picker_stockage_TYPE.value = 1

        picker_stockage_UNITE.minValue = 0
        picker_stockage_UNITE.maxValue = listeL.size - 1
        picker_stockage_UNITE.displayedValues = listeL
        picker_stockage_UNITE.value = 0

        picker_stockage_TYPE.setOnValueChangedListener { picker, oldVal, newVal ->
            when (picker.value){
                0 ->{
                    picker_stockage_UNITE.minValue = 0
                    picker_stockage_UNITE.maxValue = 0
                    picker_stockage_UNITE.displayedValues = listeN
                    picker_stockage_UNITE.value = 0
                }
                1 -> {
                    picker_stockage_UNITE.minValue = 0
                    picker_stockage_UNITE.maxValue = 0
                    picker_stockage_UNITE.displayedValues = listeL
                    picker_stockage_UNITE.value = 0
                }
            }
        }

        if (cstockage != null){
            editText_REFERENCE.setText(cstockage.reference)
            editText_CAPACITEMAX.setText(cstockage.capaDispo.toString())

            picker_stockage_TYPE.value = when (cstockage.type){
                listType[0] -> 0
                listType[1] -> 1
                listType[2] -> 2
                else -> {
                    1
                }
            }
        }
        button_save_STOCKAGE.setOnClickListener{
            // checking input text should not be null
            val valeurpickerType: String = when (picker_stockage_TYPE.value){
                0 -> listType[0]
                1 -> listType[1]
                2 -> listType[2]
                else -> {
                    listType[1]
                }
            }
            val valeurpickerUnite: String = when (picker_stockage_UNITE.value){
                0 -> "Numero"
                1 -> "m^3"
                else -> {
                    "m^3"
                }
            }
            val stockage = Cstockage(editText_REFERENCE.text.toString(), valeurpickerType, editText_CAPACITEMAX.text.toString().toInt(), cstockage!!.capaUtil, valeurpickerUnite, 0)
            val success: Boolean = dbHandler.updateDataStockage(cstockage, stockage)
            if (success){
                Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ListStockage::class.java).apply {})
                finish()
            }
            else{
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            }

        }

        button_delete_STOCKAGE.setOnClickListener {
            if (dbHandler!!.deleteDataStockage(cstockage!!.reference)){
                Toast.makeText(this,"Delete Successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ListStockage::class.java).apply {})
                finish()
            }
            else{
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ListStockage::class.java).apply {})
        finish()
    }
}
