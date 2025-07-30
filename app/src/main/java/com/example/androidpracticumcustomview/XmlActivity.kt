package com.example.androidpracticumcustomview

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer


class XmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        customContainer.layoutParams = FrameLayout.LayoutParams( // Растягиваем контейнер на весь экран
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        setContentView(customContainer)
        customContainer.setOnClickListener {
            finish()
        }

        val firstView = TextView(this).apply {
            text = "First View"
            textSize = 24f
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(Color.RED) // Специально для примера, чтобы было видно
        }

        val secondView = TextView(this).apply {
            text = "Second View"
            textSize = 24f
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(Color.RED) // Специально для примера, чтобы было видно
        }

        customContainer.addView(firstView)

        // Добавление второго элемента через некоторое время (например, по задержке)
        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 2000)
    }
}