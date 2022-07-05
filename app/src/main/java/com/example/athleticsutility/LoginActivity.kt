package com.example.athleticsutility

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun signUpRedirect(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    /*
    fun checkLogin(v: View?) {
        val email: String = editTextUserName.getText().toString()
        if (!isValidEmail(email)) {
            //Set error message for email field
            editTextUserName.setError(getString(R.string.invalid_email))
        }
        val pass: String = passwordEditText.getText().toString()
        if (!isValidPassword(pass)) {
            //Set error message for password field
            passwordEditText.setError(getString(R.string.invalid_password))
        }
        if (isValidEmail(email) && isValidPassword(pass)) {
            // Validation Completed
        }
    }
    // validating email id
    private fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    // validating password
    private fun isValidPassword(pass: String?): Boolean {
        return if (pass != null && pass.length >= 4) {
            true
        } else false
    }*/
}