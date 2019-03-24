package fr.orion.caen_ousitreham

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_boat.*

class ListBoat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_boat)

        val dbHandler: DatabaseHandler? = DatabaseHandler(this)
        val listNavire = dbHandler?.getAllNavire()

        val listnomNavire: ArrayList<String> = ArrayList()
        val listchargement: ArrayList<String> = ArrayList()
        val listeimage: ArrayList<Int> = ArrayList()

        listNavire?.forEach {
            listnomNavire.add(it.nomNavire)
            listchargement.add(it.typeFret)
            listeimage.add(it.img)
        }

        val myListAdapter = MyListAdapter(this,listnomNavire,listchargement, listeimage)
        listView_boat.adapter = myListAdapter

        listView_boat.setOnItemClickListener{ parent, view, position, id ->
            startActivity(Intent(this, InfoBoat::class.java).apply {
                putExtra("NLoyds", listNavire!![position].nLloyds)
            })
            finish()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, Menu::class.java).apply {})
        finish()
    }
}
