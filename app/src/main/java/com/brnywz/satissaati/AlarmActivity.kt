package com.brnywz.satissaati

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class AlarmActivity : Activity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var tone: ToneGenerator? = null
    private val handler = Handler(Looper.getMainLooper())
    private var stopped = false
    private var ttsReady = false

    private val repeatSpeech = object : Runnable {
        override fun run() {
            if (!stopped && ttsReady) {
                tts?.speak("SATIŞINI YAZ!!", TextToSpeech.QUEUE_FLUSH, null, "sales_alarm")
                handler.postDelayed(this, 5000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(
            android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        buildScreen()
        tts = TextToSpeech(this, this)
        tone = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        playSiren()
        vibrate()
    }

    private fun buildScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(175, 0, 0))
        }
        val message = TextView(this).apply {
            text = "🚨\\nSATIŞINI\\nYAZ!!\\n🚨"
            textSize = 42f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        val sub = TextView(this).apply {
            text = "SATIŞ GİRİŞİNİ UNUTMA!"
            textSize = 20f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 36)
        }
        val stop = Button(this).apply {
            text = "SUSTUR"
            textSize = 22f
            setOnClickListener { stopAlarm() }
        }
        listOf(message, sub, stop).forEach {
            root.addView(it, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 12 })
        }
        setContentView(root)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("tr", "TR"))
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
            if (ttsReady && !stopped) handler.post(repeatSpeech)
        }
    }

    private fun playSiren() {
        // Repeating alarm tones; stops only when the user presses SUSTUR.
        handler.post(object : Runnable {
            override fun run() {
                if (stopped) return
                tone?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 900)
                handler.postDelayed(this, 1200)
            }
        })
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val pattern = longArrayOf(0, 500, 250, 500, 250)
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, 0)
            }
        }
    }

    private fun stopAlarm() {
        stopped = true
        handler.removeCallbacksAndMessages(null)
        tts?.stop()
        tone?.stopTone()
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
        finish()
    }

    override fun onDestroy() {
        stopped = true
        handler.removeCallbacksAndMessages(null)
        tts?.stop()
        tts?.shutdown()
        tone?.release()
        super.onDestroy()
    }
}
