package com.johan.reigntest.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import com.johan.reigntest.R
import com.johan.reigntest.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val url = intent.getStringExtra("url")

        if (url == null)
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show()

        bind.wvSite.settings.javaScriptEnabled = true
        bind.wvSite.loadUrl(url)
        bind.wvSite.webViewClient = WebViewClient()
    }

    override fun onBackPressed() {
        if (bind.wvSite.canGoBack())
            bind.wvSite.goBack()
        else super.onBackPressed()
    }
}