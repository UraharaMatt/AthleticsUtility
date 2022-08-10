package com.example.athleticsutility

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

//firebase objects
private lateinit var storageReference: StorageReference
private lateinit var Database: DatabaseReference

class UploadFile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /*
    //constant to track image chooser intent
    private val PICK_IMAGE_REQUEST = 234

    //view objects
    private val buttonChoose: Button? = null
    private val buttonUpload: Button? = null
    private val editTextName: EditText? = null
    private val textViewShow: TextView? = null
    private val imageView: ImageView? = null

    //uri to store file
    private val filePath: Uri? = null
    */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_file)

        // Initialize Firebase Auth
        auth = Firebase.auth
        //storageReference = FirebaseStorage.getInstance().getReference();
       // Database = FirebaseDatabase.getInstance().getReference(Constants.)

    }



    fun showFileChooser() {
        /*val intent: Intent*/
       // intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}