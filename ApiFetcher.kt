package com.seuprojeto.trexapp.network

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiFetcher {

    fun buscarDados(codigo: String): Map<String, String>? {
        return buscarOpenLibrary(codigo) ?: buscarGoogleBooks(codigo)
    }

    private fun buscarOpenLibrary(codigo: String): Map<String, String>? {
        try {
            val url = URL("https://openlibrary.org/api/books?bibkeys=ISBN:$codigo&format=json&jscmd=data")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val data = connection.inputStream.bufferedReader().readText()
            val json = JSONObject(data)
            val book = json.optJSONObject("ISBN:$codigo") ?: return null

            val title = book.optString("title")
            val authors = book.optJSONArray("authors")?.optJSONObject(0)?.optString("name") ?: ""
            val publisher = book.optJSONArray("publishers")?.optJSONObject(0)?.optString("name") ?: ""
            val publishDate = book.optString("publish_date")
            val cover = book.optJSONObject("cover")?.optString("medium") ?: ""

            return mapOf(
                "titulo" to title,
                "autor" to authors,
                "editora" to publisher,
                "ano" to publishDate,
                "capa" to cover
            )
        } catch (_: Exception) {
            return null
        }
    }

    private fun buscarGoogleBooks(codigo: String): Map<String, String>? {
        try {
            val url = URL("https://www.googleapis.com/books/v1/volumes?q=isbn:$codigo")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val data = connection.inputStream.bufferedReader().readText()
            val json = JSONObject(data)
            val item = json.optJSONArray("items")?.optJSONObject(0) ?: return null
            val volumeInfo = item.getJSONObject("volumeInfo")

            val title = volumeInfo.optString("title")
            val authors = volumeInfo.optJSONArray("authors")?.optString(0) ?: ""
            val publisher = volumeInfo.optString("publisher") ?: ""
            val publishedDate = volumeInfo.optString("publishedDate") ?: ""
            val imageLinks = volumeInfo.optJSONObject("imageLinks")
            val thumbnail = imageLinks?.optString("thumbnail") ?: ""

            return mapOf(
                "titulo" to title,
                "autor" to authors,
                "editora" to publisher,
                "ano" to publishedDate,
                "capa" to thumbnail.replace("http://", "https://")
            )
        } catch (_: Exception) {
            return null
        }
    }
}
