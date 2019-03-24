package fr.orion.caen_ousitreham

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_boat.*

class AddBoat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_boat)

        val dbHandler: DatabaseHandler?
        //init db
            dbHandler = DatabaseHandler(this)

        val list: Array<String> = arrayOf("Conteneur", "Graine", "Liquide")

        picker.minValue = 0
        picker.maxValue = list.size - 1

        picker.displayedValues = list
        picker.value = 1


            //on Click Save button
        button_save_boat.setOnClickListener{
            // checking input text should not be null
            if (validation()){
                val valeurpickers: String = when (picker.value){
                    0 -> "Conteneur"
                    1 -> "Graine"
                    2 -> "Liquide"
                    else -> {
                        "Graine"
                    }
                }
                val bateau = Cnavire(editText_NLloyds.text.toString().toInt(), editText_nomNavire.text.toString(), editText_libelleFret.text.toString(), valeurpickers, editText_quantiteFret.text.toString().toInt(), editText_capamax.text.toString().toInt(), 0)
                val success: Boolean = dbHandler.addNavire(bateau)
                if (success){
                    Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, AddBoat::class.java).apply {})
                    finish()
                }
                else{
                    Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun validation(): Boolean{
        return if (editText_NLloyds.text.toString() != "" && editText_nomNavire.text.toString()!= "" && editText_libelleFret.text.toString()!= "" && editText_quantiteFret.text.toString() != "" && editText_capamax.text.toString() != "" && editText_quantiteFret.text.toString().toInt() <= editText_capamax.text.toString().toInt()){
            true
        } else {
            Toast.makeText(this,"All fields must be completed", Toast.LENGTH_LONG).show()
            false
        }


    }
    override fun onBackPressed() {
        startActivity(Intent(this, Menu::class.java).apply {})
        finish()
    }
}
