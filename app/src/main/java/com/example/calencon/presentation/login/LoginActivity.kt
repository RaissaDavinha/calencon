package com.example.calencon.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints.TAG
import com.example.calencon.R
import com.example.calencon.presentation.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        login_button.setOnClickListener {
            signInUser(input_email.text.toString(), input_password.text.toString())
        }

        sign_up_button.setOnClickListener {
            signUpUser()
        }
    }

    private fun signInUser(email: String, password: String) {
        progress_bar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    updateUI(auth.currentUser)
                    progress_bar.visibility = View.GONE
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    progress_bar.visibility = View.GONE
                }
            }
    }

    private fun signUpUser() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}