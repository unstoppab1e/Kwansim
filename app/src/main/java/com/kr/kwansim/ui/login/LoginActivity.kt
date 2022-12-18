package com.kr.kwansim.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kr.kwansim.R
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.ActivityLoginBinding
import com.kr.kwansim.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance();}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        val client = GoogleSignIn.getClient(this, options)

        binding.btnLogin.setOnClickListener {
            startActivityForResult(client.signInIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account: GoogleSignInAccount? = null
            try {
                account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Failed Google Login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }
}