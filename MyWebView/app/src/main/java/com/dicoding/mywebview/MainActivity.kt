package com.dicoding.mywebview

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mywebview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl("https://www.dicoding.com")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
//                Toast.makeText(this@MainActivity, "Web Dicoding berhasil dimuat", Toast.LENGTH_LONG).show()
                view.loadUrl("javascript:alert('Web Dicoding berhasil dimuat')")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }

//        webView.webChromeClient = WebChromeClient()

        webView.settings.javaScriptEnabled = true
    }
}