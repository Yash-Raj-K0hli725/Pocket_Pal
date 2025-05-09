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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketpal.MainActivity.BarcodeAnalyzer
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentOcrBinding
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors

class OCR : Fragment() {
    private lateinit var bind: FragmentOcrBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
//    private lateinit var imageCapture: ImageCapture
    private lateinit var mainViewModel: MainViewModel

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val selectImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processImageUri(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_ocr, container, false)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        val database = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(database)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

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
//            takePhoto()
        }

        bind.cvUploadFromGallery.setOnClickListener {
            selectImage.launch("image/*")
        }

        mainViewModel.barcode.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("BARCODE", "$it")
            }
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

//            imageCapture = ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { value ->
//                        Log.d("BARCODE", it.toString())
                        mainViewModel.onBarcodeScanned(value)
                    })
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
//                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

//    private fun takePhoto() {
//        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()),
//            object : ImageCapture.OnImageCapturedCallback() {
//                @OptIn(ExperimentalGetImage::class)
//                override fun onCaptureSuccess(imageProxy: ImageProxy) {
//                    Log.d("CameraX", "Capture successful")
//                    super.onCaptureSuccess(imageProxy)
//                    val mediaImage = imageProxy.image
//                    if (mediaImage != null) {
//                        val inputImage = InputImage.fromMediaImage(
//                            mediaImage, imageProxy.imageInfo.rotationDegrees
//                        )
//                        scanTextFromImage(inputImage)
//                        Log.d("CameraX", "InputImage created")
//                    }
//                    imageProxy.close()
//                }
//            })
//    }

//    private fun scanTextFromImage(image: InputImage) {
//        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//        recognizer.process(image)
//            .addOnSuccessListener { visionText ->
//                val rawText = visionText.text
//                Log.d("BARCODE", "Raw Text: $rawText")
//            }
//    }


    private fun processImageUri(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(requireActivity(), uri)
//            scanTextFromImage(image)
        } catch (e: Exception) {
            // let's see
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
