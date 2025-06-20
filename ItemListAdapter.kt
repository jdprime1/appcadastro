package com.seuprojeto.trexapp

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.seuprojeto.trexapp.model.Item
import java.net.URL
import kotlin.concurrent.thread

class ItemListAdapter(private val context: Context, private var itens: List<Item>) : BaseAdapter() {

    override fun getCount(): Int = itens.size

    override fun getItem(position: Int): Any = itens[position]

    override fun getItemId(position: Int): Long = itens[position].id.toLong()

    fun atualizar(novosItens: List<Item>) {
        this.itens = novosItens
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false)

        val item = itens[position]

        val imgCapa = view.findViewById<ImageView>(R.id.imgItemCapa)
        val txtTitulo = view.findViewById<TextView>(R.id.txtItemTitulo)
        val txtTipo = view.findViewById<TextView>(R.id.txtItemTipo)
        val txtQuantidade = view.findViewById<TextView>(R.id.txtItemQuantidade)

        txtTitulo.text = item.titulo
        txtTipo.text = item.tipo
        txtQuantidade.text = "Qtd: ${item.quantidade}"

        if (!item.capa.isNullOrEmpty()) {
            thread {
                try {
                    val stream = URL(item.capa).openStream()
                    val bmp = BitmapFactory.decodeStream(stream)
                    (context as? ListaActivity)?.runOnUiThread {
                        imgCapa.setImageBitmap(bmp)
                    }
                } catch (_: Exception) {
                }
            }
        } else {
            imgCapa.setImageResource(R.drawable.ic_no_image)
        }

        return view
    }
}
