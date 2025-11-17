package com.example.sounddetection

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.sounddetection.recorder.AudioRecorder
import com.example.sounddetection.ui.theme.SoundDetectionTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private val recorder by lazy { AudioRecorder(applicationContext) }
    private var dB: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0 )
        setContent {
            SoundDetectionTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Button(onClick = {
                    File(cacheDir, "audio.mp3").also {
                        recorder.start()
                        }
                }) {
                    Text(text = "Start recording")
                    }
                    Button(onClick = {
                        dB = recorder.stop()
                    }) {
                    Text(text = "Stop recording")
                    }
                    Text(
                        text = "dB, $dB",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}