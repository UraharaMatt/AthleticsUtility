package com.example.athleticsutility

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.athleticsutility.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    //private var TAG = "SignUp Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth
        // set on-click listener
        binding.signupButton.setOnClickListener {
            onSignUpClick()
        }

    }
    private fun onSignUpClick() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val userName = binding.nameEditText.text.toString().trim()
        val userSurname = binding.surnameEditText.text.toString().trim()

        if (userName.isEmpty()) {
            binding.nameEditText.error = "Inserisci il Nome"
            return
        }
        if (userSurname.isEmpty()) {
            binding.surnameEditText.error = "Inserisci il Cognome"
            return
        }
        if (email.isEmpty()) {
            binding.emailEditText.error = "Inserisci l email"
            return
        }
        if (password.isEmpty() || password.length < 6) {
            binding.passwordEditText.error = "Inserisci la password maggiore di 6 caratteri"
            return
        }
        createUser(userName, userSurname,email,password)
    }
    private fun createUser(userName: String, userSurname: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val currentlyUser = auth.currentUser
                    val uid = currentlyUser!!.uid
                    val newUser = User(userName, userSurname, email)
                    val database = Firebase.database("https://athleticsutility-c70a4-default-rtdb.europe-west1.firebasedatabase.app/").reference
                    database.child("users").child(uid).setValue(newUser)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "Registration:failure", task.exception)
                                Toast.makeText(baseContext, "Creation failed.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Creation failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }
    fun loginRedirect(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}