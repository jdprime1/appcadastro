package com.seuprojeto.trexapp.model

data class Item(
    val id: Int = 0,
    val codigoBarras: String,
    val titulo: String,
    val tipo: String,
    val autor: String?,
    val editora: String?,
    val ano: String?,
    val capa: String?,
    val quantidade: Int = 1
)
