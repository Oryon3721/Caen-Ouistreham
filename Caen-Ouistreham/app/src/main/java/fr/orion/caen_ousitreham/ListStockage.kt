package fr.orion.caen_ousitreham

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_stockage.*

class ListStockage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_stockage)


        val dbHandler: DatabaseHandler? = DatabaseHandler(this)
        val listStockage = dbHandler?.getAllStockage()

        val listreferenceStockage: ArrayList<String> = ArrayList()
        val listType: ArrayList<String> = ArrayList()
        val listeimage: ArrayList<Int> = ArrayList()

        listStockage?.forEach {
            listreferenceStockage.add(it.reference)
            listType.add(it.type)
            listeimage.add(it.img)
        }

        val myListAdapter = MyListAdapter(this,listreferenceStockage,listType, listeimage)
        listView_stockage.adapter = myListAdapter

        listView_stockage.setOnItemClickListener{ parent, view, position, id ->
            startActivity(Intent(this, InfoStockage::class.java).apply {
                putExtra("reference", listStockage!![position].reference)
            })
            finish()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, Menu::class.java).apply {})
        finish()
    }
}
