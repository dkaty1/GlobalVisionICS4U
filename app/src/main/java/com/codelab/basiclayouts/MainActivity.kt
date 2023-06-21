

package com.codelab.basiclayouts


import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.util.Locale


class MainActivity : ComponentActivity() {

    // This file involves creating the splash screen
    // The design was created in the corresponding XML File
    // The text to speech and onClickListeners for buttons are created here


    // Instance Variables
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speakButton: Button
    private lateinit var myButton: Button
    private lateinit var myTextView: TextView
    private lateinit var myLogo: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Set the XML
            setContentView(R.layout.lol_name)

            // Set the variables to corresponding UI element from XML
            myButton = findViewById<Button>(R.id.button)
            myTextView = findViewById<TextView>(R.id.textView2)
            myLogo = findViewById<ImageView>(R.id.imageView2)

            textToSpeech = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    // TextToSpeech engine is successfully initialized
                    textToSpeech.language = Locale.ENGLISH
                    myButton.isEnabled = true
                    val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    val increasedVolume = (maxVolume * 20).toInt() // Increase by 80%
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)

                    // Listener for button
                    myButton.setOnHoverListener { view, event ->
                        when (event.action) {
                            // Start speech synthesis when hovering over the button
                            android.view.MotionEvent.ACTION_HOVER_ENTER -> {
                                val text = "Button text to speech"
                                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                            // Stop speech synthesis when hover ends
                            android.view.MotionEvent.ACTION_HOVER_EXIT -> {
                                textToSpeech.stop()
                            }
                        }
                        true
                    }

                    // Listener for clicking or touching a text or image
                    myTextView.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            when (event?.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    // Touch event started, speak the text
                                    val text = myTextView.text.toString()
                                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                                }
                            }
                            return true
                        }
                    })

                    // Setting the onTouchListener for the GlobalVision logo
                    myLogo.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            when (event?.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    val text = "Global Vision"
                                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                                }
                            }
                            return true
                        }
                    })

                } else {
                    // TextToSpeech engine initialization failed
                    myButton.isEnabled = false
                }
            }



            // Setting the onClickListener for the button
            // Alows user to navigate from splash screen to log in page
            myButton?.setOnClickListener {

                val text = "Continue"
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                val intent = Intent(this@MainActivity, LogIn::class.java)
                startActivity(intent)
            }







        }



    }

    // closing the text to speech to save resources

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown() // Shutdown the TextToSpeech engine to release resources
    }




}