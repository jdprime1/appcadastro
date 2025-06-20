package com.seuprojeto.trexapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var btnCadastrar: Button
    private lateinit var btnListar: Button
    private lateinit var btnToggleTheme: Button
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnListar = findViewById(R.id.btnListar)
        btnToggleTheme = findViewById(R.id.btnToggleTheme)

        btnCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        btnListar.setOnClickListener {
            startActivity(Intent(this, ListaActivity::class.java))
        }

        btnToggleTheme.setOnClickListener {
            val newMode = if (prefs.getBoolean("dark_mode", false)) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            prefs.edit().putBoolean("dark_mode", newMode == AppCompatDelegate.MODE_NIGHT_YES).apply()
            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }
}
