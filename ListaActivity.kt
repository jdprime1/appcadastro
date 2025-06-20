package com.seuprojeto.trexapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seuprojeto.trexapp.db.DBHelper
import com.seuprojeto.trexapp.model.Item

class ListaActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var db: DBHelper
    private lateinit var adapter: ItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        db = DBHelper(this)
        listView = findViewById(R.id.listViewItens)

        val itens = db.listarItens()
        adapter = ItemListAdapter(this, itens)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position) as Item
            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("item_id", item.id)
            startActivity(intent)
        }

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            val item = adapter.getItem(position) as Item
            val deleted = db.removerItem(item.id)
            if (deleted > 0) {
                Toast.makeText(this, "Item removido", Toast.LENGTH_SHORT).show()
                adapter.atualizar(db.listarItens())
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.atualizar(db.listarItens())
    }
}
