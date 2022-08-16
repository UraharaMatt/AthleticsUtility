package com.example.athleticsutility

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.athleticsutility.databinding.ActivityLoginBinding
import com.example.athleticsutility.databinding.ActivityMainBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.CallbackManager.*
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private lateinit var auth: FirebaseAuth
private lateinit var googleSignInClient: GoogleSignInClient
private const val RC_SIGN_IN = 9001
private lateinit var resultGoogleLauncher: ActivityResultLauncher<Intent>
private lateinit var resultFBLauncher: ActivityResultLauncher<Intent>
private lateinit var callbackManagerFB: CallbackManager
//private lateinit var buttonFacebookLogin: LoginButton
//[binding init
private lateinit var binding: ActivityLoginBinding




class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        // [START config_signin]

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]
        resultGoogleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != null){ //= RC_SIGN_IN) {
                // There are no request codes
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken)
                } catch (e: ApiException) {
                    Log.d(TAG, "not Work")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        binding.loginButton.setOnClickListener{
            onLoginClick()
        }
        binding.loginGoogle.setOnClickListener {
            onGoogleLogClick()
        }
        binding.loginFB.setOnClickListener {/*
            // Initialize Facebook Login button
            callbackManagerFB = Factory.create()
            buttonFacebookLogin.setReadPermissions("email", "public_profile")
            buttonFacebookLogin.registerCallback(callbackManagerFB, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            })
*/
        }
        binding.signUpTextView.setOnClickListener {
            signUpRedirect()
        }
        binding.forgottenPasswordTextView.setOnClickListener{
            onRecoveryPasswordClick()
        }
    }
    private fun onLoginClick() {
        val email = binding.emailLogEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailLogEditText.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Enter password"
            return
        }
        loginUser(email, password)
    }
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun onGoogleLogClick()
    {
        val signInIntent = googleSignInClient.signInIntent
        Log.d(TAG, signInIntent.toString())
        resultGoogleLauncher.launch(signInIntent)
    }
    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    //val user = auth.currentUser
                    //updateUI(user)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }
    // [END auth_with_google]
    private fun onFBLogClick()
    {
       // resultFBLauncher.launch()
    }
    // [START auth_with_FB]
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }
    // [END auth_with_FB]

    private fun onRecoveryPasswordClick() {
        /*val intent = Intent(this, RecoveryPassword::class.java)
        startActivity(intent)
        finish()*/
    }
    fun signUpRedirect() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

}