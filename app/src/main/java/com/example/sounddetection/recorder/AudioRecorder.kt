package com.example.sounddetection.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlin.math.log10

class AudioRecorder(
    private val context: Context
) {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        } else MediaRecorder()
    }

    fun getDB(): Double{
        val amp = recorder!!.maxAmplitude.toDouble()
        return if (amp > 0){
            20 * log10(amp / 32767.0)
        } else {
            -Double.POSITIVE_INFINITY
        }
    }

    fun start(){
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("dev/null")

            prepare()
            start()

            recorder = this
        }
    }

    fun stop(): Double {
        recorder?.stop()
        recorder?.reset()
        recorder = null
        return this.getDB()
    }
}