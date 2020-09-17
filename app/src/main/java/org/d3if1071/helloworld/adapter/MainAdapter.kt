package org.d3if1071.helloworld.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_main.view.*
import org.d3if1071.helloworld.R
import org.d3if1071.helloworld.models.Harian
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter :RecyclerView.Adapter<MainAdapter.ViewHolder>(){

    private val formatter = SimpleDateFormat("dd MMMM", Locale("ID", "id"))
    private val data = ArrayList<Harian>()

    //view holder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tanggalTextView = itemView.findViewById<TextView>(R.id.tanggal)
        private val positifTextView = itemView.findViewById<TextView>(R.id.jumlahPositif)

        fun bind(harian: Harian){
            tanggalTextView.text = formatter.format(Date((harian.key)))
            positifTextView.text = itemView.context.getString(R.string.x_orang,harian.jumlahPositif.value)
        }
    }

    //set data
    fun setData(newData:List<Harian>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatter = LayoutInflater.from(parent.context)
        val view = inflatter.inflate(R.layout.list_item_main,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    //data untuk chart
    fun getDate(position: Int):Date{
        return Date(data[position].key)
    }
}