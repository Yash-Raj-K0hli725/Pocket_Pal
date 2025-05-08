package com.example.pocketpal.MainActivity.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentOcrBinding
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OCR : Fragment() {
    private lateinit var bind: FragmentOcrBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture

    private val selectImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processImageUri(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_ocr, container, false)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCamera()

        bind.cvCamera.setOnClickListener {
            bind.apply {
                progressBar.visibility = View.VISIBLE
                cvUploadFromGallery.alpha = 0.7f
                cvCamera.alpha = 0.7f
            }
            takePhoto()
        }

        bind.cvUploadFromGallery.setOnClickListener {
            selectImage.launch("image/*")
        }
    }

    private fun startCamera() {
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview: Preview = Preview.Builder().build().also {
                it.surfaceProvider = bind.previewView.surfaceProvider
            }

            val cameraSelector: CameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

//            var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun takePhoto() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    Log.d("CameraX", "Capture successful")
                    super.onCaptureSuccess(imageProxy)
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage, imageProxy.imageInfo.rotationDegrees
                        )
                        scanImage(inputImage)
                        Log.d("CameraX", "InputImage created")
                    }
                    imageProxy.close()
                }
            })
    }

    private fun processImageUri(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(requireContext(), uri)
            scanImage(image)
        } catch (e: Exception) {
            // let's see
        }
    }

    private fun scanImage(image: InputImage) {
        recognizeText(image)
            .addOnSuccessListener { visionText ->
                parseAndEmit(visionText)
            }
    }

    private fun recognizeText(image: InputImage): Task<Text> {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        return recognizer.process(image)
    }

    private fun parseAndEmit(visionText: Text) {
        val rawText = visionText.text
        Log.d("charu", "FULL TEXT:\n$rawText")
        val amounts = Regex("\\d+(?:\\.\\d+)?")
            .findAll(rawText)
            .map { it.value.toFloatOrNull() ?: 0f }
            .filter { it > 0f }
            .toList()

        val lastAmount = if (amounts.isNotEmpty()) {
            // Check if the last number is an integer (i.e., no decimal point)
            if (amounts.last() == amounts.last().toInt().toFloat()) {
                // If it's an integer, take the second last float (if it exists)
                amounts.getOrElse(amounts.size - 2) { amounts.last() }
            } else {
                // Otherwise, take the last float number
                amounts.last()
            }
        } else {
            0f
        }

        Log.d("charu", "Total Found: $lastAmount")
    }
}
