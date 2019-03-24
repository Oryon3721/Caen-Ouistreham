package fr.orion.caen_ousitreham

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import fr.orion.caen_ouistreham.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import kotlinx.android.synthetic.main.content_menu.*

class Menu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)






        val dbHandler: DatabaseHandler? = DatabaseHandler(this)
        val listStockage = dbHandler?.getAllStockage()

        val listreferenceStockage: ArrayList<String> = ArrayList()
        val listValeur: ArrayList<Int> = ArrayList()
        val listeimage: ArrayList<Int> = ArrayList()

        listStockage?.forEach {
            listreferenceStockage.add(it.reference)
            listValeur.add(it.QuelPourcentage().toInt())
            listeimage.add(it.img)
        }

        val myListAdapter = MyListAdapterMenu(this,listreferenceStockage,listValeur, listeimage)
        listView_stockage_MENU.adapter = myListAdapter

        listView_stockage_MENU.setOnItemClickListener{ parent, view, position, id ->
            startActivity(Intent(this, GestionStockage::class.java).apply {
                putExtra("reference", listStockage!![position].reference)
            })
            finish()
        }





    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.add_boat -> {
                startActivity(Intent(this, AddBoat::class.java).apply {})
                finish()
            }
            R.id.list_ship -> {
                startActivity(Intent(this, ListBoat::class.java).apply {})
                finish()
            }
            R.id.add_storage_area -> {
                startActivity(Intent(this, AddStockage::class.java).apply {})
                finish()
            }
            R.id.list_storage_area -> {
                startActivity(Intent(this, ListStockage::class.java).apply {})
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
