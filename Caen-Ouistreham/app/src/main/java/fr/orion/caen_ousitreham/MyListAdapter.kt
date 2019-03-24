package fr.orion.caen_ousitreham

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class MyListAdapter(private val context: Activity, private val title: ArrayList<String>, private val description: ArrayList<String>, private val image: ArrayList<Int>): ArrayAdapter<String>(context, R.layout.list_personalise, title) {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_personalise, null, true)


        val titleText = rowView.findViewById(R.id._Titre) as TextView
        val imageView = rowView.findViewById(R.id._icone) as ImageView
        val subtitleText = rowView.findViewById(R.id._Description) as TextView


        titleText.text = title[position]
        imageView.setImageResource(image[position])
        subtitleText.text = description[position]



        return rowView
    }
}

class MyListAdapterMenu(private val context: Activity, private val title: ArrayList<String>, private val valeur: ArrayList<Int>, private val image: ArrayList<Int>): ArrayAdapter<String>(context, R.layout.list_personalise_menu, title) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_personalise_menu, null, true)


        val titleText = rowView.findViewById(R.id._Titre) as TextView
        val imageView = rowView.findViewById(R.id._icone) as ImageView
        val progress = rowView.findViewById(R.id._progress) as ProgressBar
        val pourcentage = rowView.findViewById(R.id._pourcentage) as TextView


        titleText.text = title[position]
        imageView.setImageResource(image[position])
        progress.progress = valeur[position]
        when {
            valeur[position] <= 50 -> progress.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
            valeur[position] <= 75 -> progress.progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
            valeur[position] <= 95 -> progress.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
            valeur[position] <= 100 -> progress.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
        }
        pourcentage.text = valeur[position].toString() + "%"



        return rowView
    }
}
