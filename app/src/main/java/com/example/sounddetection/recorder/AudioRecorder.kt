package com.example.sounddetection.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlin.math.log10

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    fun getDB(): Double {
        val amp = recorder?.maxAmplitude ?: return 0.0

        if (amp <= 0) return 0.0

        val dbRelative = 20 * log10(amp / 32767.0)

        val dbReal = (dbRelative + 90)

        return dbReal
    }

    fun start(outputPath: String) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            setOutputFile(outputPath)

            prepare()
            start()

            recorder = this
        }
    }

    fun stop(): Double {
        val r = recorder ?: return -Double.POSITIVE_INFINITY

        r.stop()
        val db = getDB()
        r.reset()

        recorder = null
        return db
    }
}
