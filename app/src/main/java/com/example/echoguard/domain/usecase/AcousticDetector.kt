@file:Suppress("SpellCheckingInspection")
package com.example.echoguard.domain.usecase

import android.content.Context
import android.util.Log
import org.tensorflow.lite.task.audio.classifier.AudioClassifier

/**
 * AcousticDetector: Processes real-time audio distress signals.
 * FIX: Captured classifier locally to resolve reference issues.
 */
class AcousticDetector(private val context: Context, private val onTrigger: () -> Unit) {

    private var isSensing = false
    private var classifier: AudioClassifier? = null

    fun startSensing() {
        if (isSensing) return
        isSensing = true

        Thread {
            try {
                // Ensure 'yamnet.tflite' is in app/src/main/assets/
                val currentClassifier = AudioClassifier.createFromFile(context, "yamnet.tflite")
                classifier = currentClassifier

                val audioRecord = currentClassifier.createAudioRecord()
                audioRecord.startRecording()

                while (isSensing) {
                    // FIX: This method will be found once Gradle Sync is successful.
                    val audioTensor = currentClassifier.createInputTensorAudio()
                    audioTensor.load(audioRecord)

                    val results = currentClassifier.classify(audioTensor)

                    val detected = results.flatMap { it.categories }.find {
                        (it.label == "Scream" || it.label == "Crying") && it.score > 0.80f
                    }

                    if (detected != null) {
                        Log.d("EchoGuard", "AI DISTRESS CONFIRMED")
                        onTrigger()
                        break
                    }
                    Thread.sleep(47)
                }
                audioRecord.stop()
            } catch (e: Exception) {
                Log.e("EchoGuard", "Acoustic AI Fail: ${e.message}")
            }
        }.start()
    }

    fun stopSensing() {
        isSensing = false
        classifier = null
    }
}