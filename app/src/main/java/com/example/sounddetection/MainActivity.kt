package com.example.sounddetection

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.sounddetection.recorder.AudioRecorder
import com.example.sounddetection.ui.theme.SoundDetectionTheme
import kotlinx.coroutines.delay
import java.io.File

class MainActivity : ComponentActivity() {

    private val recorder by lazy { AudioRecorder(applicationContext) }

    private var startRecordingCallback: (() -> Unit)? = null

    private val requestAudioPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startRecordingCallback?.invoke()
            } else {
                println("Permissão negada.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var isRecording by remember { mutableStateOf(false) }
            var dB by remember { mutableStateOf(0.0) }

            LaunchedEffect(isRecording) {
                while (isRecording) {
                    dB = recorder.getDB()
                    delay(100)
                }
            }

            SoundDetectionTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (isRecording) {
                        Text("Recording...", fontSize = 22.sp)
                    }

                    Button(onClick = {
                        startRecordingCallback = {
                            startRecording()
                            isRecording = true
                        }
                        requestAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
                    }) {
                        Text("Start recording")
                    }

                    Button(onClick = {
                        recorder.stop()
                        isRecording = false
                    }) {
                        Text("Stop recording")
                    }

                    Text(
                        text = "dB: ${"%.1f".format(dB)}",
                        fontSize = 22.sp
                    )
                }
            }
        }
    }

    private fun startRecording() {
        val output = File(cacheDir, "audio.mp4")
        recorder.start(output.absolutePath)
    }
}
