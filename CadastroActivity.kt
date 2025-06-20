package com.seuprojeto.trexapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.seuprojeto.trexapp.db.DBHelper
import com.seuprojeto.trexapp.model.Item
import com.seuprojeto.trexapp.network.ApiFetcher
import java.net.URL
import kotlin.concurrent.thread

class CadastroActivity : AppCompatActivity() {

    private lateinit var edtCodigo: EditText
    private lateinit var edtTitulo: EditText
    private lateinit var edtTipo: Spinner
    private lateinit var edtAutor: EditText
    private lateinit var edtEditora: EditText
    private lateinit var edtAno: EditText
    private lateinit var edtQuantidade: EditText
    private lateinit var edtCapa: EditText
    private lateinit var imgCapa: ImageView
    private lateinit var btnBuscar: Button
    private lateinit var btnSalvar: Button

    private var itemId: Int? = null
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        db = DBHelper(this)

        edtCodigo = findViewById(R.id.edtCodigo)
        edtTitulo = findViewById(R.id.edtTitulo)
        edtTipo = findViewById(R.id.edtTipo)
        edtAutor = findViewById(R.id.edtAutor)
        edtEditora = findViewById(R.id.edtEditora)
        edtAno = findViewById(R.id.edtAno)
        edtQuantidade = findViewById(R.id.edtQuantidade)
        edtCapa = findViewById(R.id.edtCapa)
        imgCapa = findViewById(R.id.imgCapa)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnSalvar = findViewById(R.id.btnSalvar)

        val tipos = listOf("Livro", "HQ", "DVD", "CD", "Blu-ray", "Jogo")
        edtTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        itemId = intent.getIntExtra("item_id", -1).takeIf { it != -1 }
        itemId?.let {
            val item = db.buscarItemPorId(it)
            item?.let { preencherCampos(it) }
        }

        btnBuscar.setOnClickListener {
            val codigo = edtCodigo.text.toString().trim()
            if (codigo.isNotEmpty()) {
                buscarDadosOnline(codigo)
            }
        }

        btnSalvar.setOnClickListener {
            salvarItem()
        }
    }

    private fun preencherCampos(item: Item) {
        edtCodigo.setText(item.codigoBarras)
        edtTitulo.setText(item.titulo)
        edtTipo.setSelection((edtTipo.adapter as ArrayAdapter<String>).getPosition(item.tipo))
        edtAutor.setText(item.autor)
        edtEditora.setText(item.editora)
        edtAno.setText(item.ano)
        edtQuantidade.setText(item.quantidade.toString())
        edtCapa.setText(item.capa)
        carregarCapa(item.capa)
    }

    private fun salvarItem() {
        val item = Item(
            id = itemId ?: 0,
            codigoBarras = edtCodigo.text.toString().trim(),
            titulo = edtTitulo.text.toString().trim(),
            tipo = edtTipo.selectedItem.toString(),
            autor = edtAutor.text.toString().trim(),
            editora = edtEditora.text.toString().trim(),
            ano = edtAno.text.toString().trim(),
            capa = edtCapa.text.toString().trim(),
            quantidade = edtQuantidade.text.toString().toIntOrNull() ?: 1
        )

        if (itemId != null) {
            db.atualizarItem(item)
        } else {
            db.inserirItem(item)
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun buscarDadosOnline(codigo: String) {
        thread {
            val dados = ApiFetcher.buscarDados(codigo)
            runOnUiThread {
                if (dados != null) {
                    edtTitulo.setText(dados["titulo"])
                    edtAutor.setText(dados["autor"])
                    edtEditora.setText(dados["editora"])
                    edtAno.setText(dados["ano"])
                    edtCapa.setText(dados["capa"])
                    carregarCapa(dados["capa"])
                } else {
                    Toast.makeText(this, "Não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun carregarCapa(url: String?) {
        if (url.isNullOrEmpty()) return
        thread {
            try {
                val input = URL(url).openStream()
                val bmp = BitmapFactory.decodeStream(input)
                runOnUiThread {
                    imgCapa.setImageBitmap(bmp)
                }
            } catch (_: Exception) {}
        }
    }
}
