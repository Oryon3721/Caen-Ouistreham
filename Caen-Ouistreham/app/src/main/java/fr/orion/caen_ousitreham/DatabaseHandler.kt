@file:Suppress("UNUSED_CHANGED_VALUE")

package fr.orion.caen_ousitreham

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_NAVIRE = "CREATE TABLE $TABLE_NAME_NAVIRE " +
                "($ID_NAVIRE Integer PRIMARY KEY, $NLLOYDS Integer, $NAME_NAV TEXT, $LIBELLE_FRET TEXT, $TYPE_FRET TEXT, $QTE_FRET INTEGER, $CAPA_MAX_NAVIRE INTEGER)"
        db?.execSQL(CREATE_TABLE_NAVIRE)
        val CREATE_TABLE_STOCKAGE = "CREATE TABLE $TABLE_NAME_STOCKAGE " +
                "($ID_STOCKAGE Integer PRIMARY KEY, $REFERENCE TEXT, $TYPE_STOCKAGE TEXT, $CAPA_DISPO_STOCKAGE INTEGER, $CAPA_UTIL_STOCKAGE INTEGER, $UNITE_STOCKAGE TEXT)"
        db?.execSQL(CREATE_TABLE_STOCKAGE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    fun addNavire(navire: Cnavire): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NLLOYDS, navire.nLloyds)
        values.put(NAME_NAV, navire.nomNavire)
        values.put(LIBELLE_FRET, navire.libelleFret)
        values.put(TYPE_FRET, navire.typeFret)
        values.put(QTE_FRET, navire.qteFret)
        values.put(CAPA_MAX_NAVIRE, navire.capaMaxN)
        val _success = db.insert(TABLE_NAME_NAVIRE, null, values)
        db.close()
        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getAllNavire(): ArrayList<Cnavire> {
        val listNavire: ArrayList<Cnavire> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_NAVIRE"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var nLloyds = cursor.getInt(cursor.getColumnIndex(NLLOYDS))
                    var nomNavire = cursor.getString(cursor.getColumnIndex(NAME_NAV))
                    var libelleFret = cursor.getString(cursor.getColumnIndex(LIBELLE_FRET))
                    var typeFret = cursor.getString(cursor.getColumnIndex(TYPE_FRET))
                    var qteFret = cursor.getInt(cursor.getColumnIndex(QTE_FRET))
                    var capaMax = cursor.getInt(cursor.getColumnIndex(CAPA_MAX_NAVIRE))
                    var img = when (typeFret) {
                        "Graine","Liquide" -> R.drawable.bateau
                        "Conteneur" -> R.drawable.bateau2
                        else -> {
                            R.drawable.bar_boat
                        }
                    }
                    var cnavire = Cnavire(nLloyds, nomNavire, libelleFret, typeFret, qteFret, capaMax, img)
                    listNavire.add(cnavire)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return listNavire
    }

    fun getOneNavire(numatrouver: Int): Cnavire {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_NAVIRE WHERE $NLLOYDS = $numatrouver"
        val cursor = db.rawQuery(selectALLQuery, null)
        cursor.moveToFirst()
        val nLloyds = cursor.getInt(cursor.getColumnIndex(NLLOYDS))
        val nomNavire = cursor.getString(cursor.getColumnIndex(NAME_NAV))
        val libelleFret = cursor.getString(cursor.getColumnIndex(LIBELLE_FRET))
        val typeFret = cursor.getString(cursor.getColumnIndex(TYPE_FRET))
        val qteFret = cursor.getInt(cursor.getColumnIndex(QTE_FRET))
        var capaMax = cursor.getInt(cursor.getColumnIndex(CAPA_MAX_NAVIRE))
        val img = when (typeFret) {
            "Graine","Liquide" -> R.drawable.bateau
            "Conteneur" -> R.drawable.bateau2
            else -> {
                R.drawable.bar_boat
            }
        }
        cursor.close()
        db.close()
        return Cnavire(nLloyds, nomNavire, libelleFret, typeFret, qteFret, capaMax, img)
    }

    fun getSpecificalNavire(typestockage: String): ArrayList<Cnavire> {
        val typefret: String = when (typestockage){
            "Conteneur" -> "Conteneur"
            "Silo" -> "Graine"
            "Liquide" -> "Liquide"
            else -> {
                "Graine"
            }
        }
        val listNavire: ArrayList<Cnavire> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_NAVIRE WHERE $TYPE_FRET = '$typefret' ORDER BY $NLLOYDS ASC"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var nLloyds = cursor.getInt(cursor.getColumnIndex(NLLOYDS))
                    var nomNavire = cursor.getString(cursor.getColumnIndex(NAME_NAV))
                    var libelleFret = cursor.getString(cursor.getColumnIndex(LIBELLE_FRET))
                    var typeFret = cursor.getString(cursor.getColumnIndex(TYPE_FRET))
                    var qteFret = cursor.getInt(cursor.getColumnIndex(QTE_FRET))
                    var capaMax = cursor.getInt(cursor.getColumnIndex(CAPA_MAX_NAVIRE))
                    var img = when (typeFret) {
                        "Graine","Liquide" -> R.drawable.bateau
                        "Conteneur" -> R.drawable.bateau2
                        else -> {
                            R.drawable.bar_boat
                        }
                    }
                    var cnavire = Cnavire(nLloyds, nomNavire, libelleFret, typeFret, qteFret, capaMax, img)
                    listNavire.add(cnavire)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return listNavire
    }

    fun updateDataNavire(navire: Cnavire): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NLLOYDS, navire.nLloyds)
        values.put(NAME_NAV, navire.nomNavire)
        values.put(LIBELLE_FRET, navire.libelleFret)
        values.put(TYPE_FRET, navire.typeFret)
        values.put(QTE_FRET, navire.qteFret)
        values.put(CAPA_MAX_NAVIRE, navire.capaMaxN)
        val _success = db.update(TABLE_NAME_NAVIRE, values, "$NLLOYDS=?", arrayOf(navire.nLloyds.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteDataNavire(nlloyds: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME_NAVIRE, "$NLLOYDS=?", arrayOf(nlloyds.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addStockage(stockage: Cstockage): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(REFERENCE, stockage.reference)
        values.put(TYPE_STOCKAGE, stockage.type)
        values.put(CAPA_DISPO_STOCKAGE, stockage.capaDispo)
        values.put(CAPA_UTIL_STOCKAGE, 0)
        values.put(UNITE_STOCKAGE, stockage.unite)
        val _success = db.insert(TABLE_NAME_STOCKAGE, null, values)
        db.close()
        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getAllStockage(): ArrayList<Cstockage> {
        val listStockage: ArrayList<Cstockage> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_STOCKAGE ORDER BY $REFERENCE ASC "
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var reference = cursor.getString(cursor.getColumnIndex(REFERENCE))
                    var typestockage = cursor.getString(cursor.getColumnIndex(TYPE_STOCKAGE))
                    var capadispo = cursor.getInt(cursor.getColumnIndex(CAPA_DISPO_STOCKAGE))
                    var capautil = cursor.getInt(cursor.getColumnIndex(CAPA_UTIL_STOCKAGE))
                    var unitestockage = cursor.getString(cursor.getColumnIndex(UNITE_STOCKAGE))
                    var img = when (typestockage) {
                        "Silo" -> R.drawable.silo
                        "Liquide" -> R.drawable.silo2
                        "Conteneur" -> R.drawable.conteneur
                        else -> {
                            R.drawable.silo2
                        }
                    }
                    var cstokage = Cstockage(reference, typestockage, capadispo, capautil, unitestockage, img)
                    listStockage.add(cstokage)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return listStockage
    }

    fun getOneStockage(refStockage: String): Cstockage {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_STOCKAGE WHERE $REFERENCE = '$refStockage'"
        val cursor = db.rawQuery(selectALLQuery, null)
        cursor.moveToFirst()
        val reference = cursor.getString(cursor.getColumnIndex(REFERENCE))
        val typestockage = cursor.getString(cursor.getColumnIndex(TYPE_STOCKAGE))
        val capadispo = cursor.getInt(cursor.getColumnIndex(CAPA_DISPO_STOCKAGE))
        val capautil = cursor.getInt(cursor.getColumnIndex(CAPA_UTIL_STOCKAGE))
        val unitestockage = cursor.getString(cursor.getColumnIndex(UNITE_STOCKAGE))
        val img = when (typestockage) {
            "Silo" -> R.drawable.silo
            "Liquide" -> R.drawable.silo2
            "Conteneur" -> R.drawable.conteneur
            else -> {
                R.drawable.silo2
            }
        }
        cursor.close()
        db.close()
        return Cstockage(reference, typestockage, capadispo, capautil, unitestockage, img)
    }

    fun updateDataStockage(Holdstockage: Cstockage, stockage: Cstockage): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(REFERENCE, stockage.reference)
        values.put(TYPE_STOCKAGE, stockage.type)
        values.put(CAPA_DISPO_STOCKAGE, stockage.capaDispo)
        values.put(CAPA_UTIL_STOCKAGE, stockage.capaUtil)
        values.put(UNITE_STOCKAGE, stockage.unite)
        val _success = db.update(TABLE_NAME_STOCKAGE, values, "$REFERENCE=?", arrayOf(Holdstockage.reference)).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteDataStockage(refeence: String): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME_STOCKAGE, "$REFERENCE=?", arrayOf(refeence)).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private const val DB_NAME = "Caen_Ouis"
        private const val DB_VERSIOM = 1

        private const val TABLE_NAME_NAVIRE = "navire"

        private const val ID_NAVIRE = "idN"
        private const val NLLOYDS = "Lloyds"
        private const val NAME_NAV = "NameNavire"
        private const val LIBELLE_FRET = "LibelleFret"
        private const val TYPE_FRET = "TypeFret"
        private const val QTE_FRET = "QuantiteFret"
        private const val CAPA_MAX_NAVIRE = "CapaciteMaxNavire"


        private const val TABLE_NAME_STOCKAGE = "stockage"

        private const val ID_STOCKAGE = "idS"
        private const val REFERENCE = "Reference"
        private const val TYPE_STOCKAGE = "TypeStockage"
        private const val CAPA_DISPO_STOCKAGE = "CapaDispoStockage"
        private const val CAPA_UTIL_STOCKAGE = "CapaUtilStockage"
        private const val UNITE_STOCKAGE = "UniteStockage"
    }
}