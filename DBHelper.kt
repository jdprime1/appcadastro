package com.seuprojeto.trexapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.seuprojeto.trexapp.model.Item

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "trex_items.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "itens"
        const val COL_ID = "id"
        const val COL_CODIGO = "codigo_barras"
        const val COL_TITULO = "titulo"
        const val COL_TIPO = "tipo"
        const val COL_AUTOR = "autor"
        const val COL_EDITORA = "editora"
        const val COL_ANO = "ano"
        const val COL_CAPA = "capa"
        const val COL_QUANTIDADE = "quantidade"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CODIGO TEXT,
                $COL_TITULO TEXT NOT NULL,
                $COL_TIPO TEXT NOT NULL,
                $COL_AUTOR TEXT,
                $COL_EDITORA TEXT,
                $COL_ANO TEXT,
                $COL_CAPA TEXT,
                $COL_QUANTIDADE INTEGER DEFAULT 1
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun inserirItem(item: Item): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CODIGO, item.codigoBarras)
            put(COL_TITULO, item.titulo)
            put(COL_TIPO, item.tipo)
            put(COL_AUTOR, item.autor)
            put(COL_EDITORA, item.editora)
            put(COL_ANO, item.ano)
            put(COL_CAPA, item.capa)
            put(COL_QUANTIDADE, item.quantidade)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun atualizarItem(item: Item): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CODIGO, item.codigoBarras)
            put(COL_TITULO, item.titulo)
            put(COL_TIPO, item.tipo)
            put(COL_AUTOR, item.autor)
            put(COL_EDITORA, item.editora)
            put(COL_ANO, item.ano)
            put(COL_CAPA, item.capa)
            put(COL_QUANTIDADE, item.quantidade)
        }
        return db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(item.id.toString()))
    }

    fun removerItem(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun listarItens(): List<Item> {
        val db = readableDatabase
        val lista = mutableListOf<Item>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val item = Item(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    codigoBarras = cursor.getString(cursor.getColumnIndexOrThrow(COL_CODIGO)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO)),
                    autor = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTOR)),
                    editora = cursor.getString(cursor.getColumnIndexOrThrow(COL_EDITORA)),
                    ano = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANO)),
                    capa = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAPA)),
                    quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE))
                )
                lista.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun buscarItemPorId(id: Int): Item? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_ID = ?", arrayOf(id.toString()))
        var item: Item? = null
        if (cursor.moveToFirst()) {
            item = Item(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                codigoBarras = cursor.getString(cursor.getColumnIndexOrThrow(COL_CODIGO)),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO)),
                autor = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTOR)),
                editora = cursor.getString(cursor.getColumnIndexOrThrow(COL_EDITORA)),
                ano = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANO)),
                capa = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAPA)),
                quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE))
            )
        }
        cursor.close()
        return item
    }
} 
