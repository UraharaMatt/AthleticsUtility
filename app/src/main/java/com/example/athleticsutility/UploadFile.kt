package com.example.athleticsutility

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.athleticsutility.databinding.ActivityUploadFileBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.IOException


private lateinit var binding : ActivityUploadFileBinding

class UploadFile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    // [START storage_field_declaration]
    private lateinit var storage: FirebaseStorage
    // [END storage_field_declaration]
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_file)
        binding = ActivityUploadFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [START storage_field_initialization]
        storage = Firebase.storage
        // [END storage_field_initialization]
        // Create a storage reference from our app
        val storageRef = storage.reference.child("Uploads/")

        //val uploadRef = storageRef.child("Uploads/")

        val launchUploadActivity: ActivityResultLauncher<Intent> = registerForActivityResult(StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                // do your operation from here....
                if (data != null && data.data != null) {
                    filePath = data.data
                    val selectedImageBitmap: Bitmap
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
                        binding.imageView.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }


        binding.buttonChoose.setOnClickListener{
            showFileChooser(launchUploadActivity)
        }
        binding.buttonUpload.setOnClickListener{
            onUploadClick(storageRef)
        }
        binding.textViewShow.setOnClickListener {
            uploadView()
        }
    }

    private fun uploadView() {
        TODO("Not yet implemented")
    }
    private fun onUploadClick(storageRef: StorageReference) {
        //Check Name of file
        val nomeFile = binding.eTXTNomeFile.text.toString().trim()
        if (nomeFile.isEmpty()) {
            binding.eTXTNomeFile.error = "Inserire il nome dell'atleta"
            return
        }
        //checking if file is available
        if (filePath != null) {
            //getting the storage reference

            val uploadRef = storageRef.child( nomeFile + "." + getFileExtension(filePath))
            //adding the file to reference
            uploadRef.putFile(filePath!!)
                .addOnSuccessListener{ taskSnapshot ->
                //displaying success toast
                    Toast.makeText(applicationContext, "File Uploaded ", Toast.LENGTH_LONG).show();

                }
                .addOnFailureListener {
                    // Handle unsuccessful uploads
                    Toast.makeText(applicationContext, "File Not Uploaded ", Toast.LENGTH_LONG).show();

                }
        }
        else{//display an error if no file is selected
            Toast.makeText(applicationContext, "File not Selected ", Toast.LENGTH_LONG).show();

        }

    }
    fun showFileChooser( launchUploadActivity:ActivityResultLauncher<Intent>) {

        val intentUp = Intent()
        intentUp.type = "image/*"
        intentUp.action = Intent.ACTION_GET_CONTENT
        launchUploadActivity.launch(intentUp)
    }
    fun getFileExtension(uri: Uri?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }

}