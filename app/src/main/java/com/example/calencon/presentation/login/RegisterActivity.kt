package com.example.calencon.presentation.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import com.example.calencon.R
import com.example.calencon.data.USERS_DOC
import com.example.calencon.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.register_activity.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        sign_up_button.setOnClickListener {
            input_password.text?.let { password ->
                if (password.length >= 6) {
                    if (password.toString() == input_confirm_password.text.toString()) {
                        signUpUser(
                            input_user_name.text.toString(),
                            input_email.text.toString(),
                            input_password.text.toString()
                        )
                    } else {
                        Toast.makeText(
                            baseContext,
                            R.string.passwords_not_match,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        baseContext, R.string.password_need_at_least_six, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        select_photo_button.setOnClickListener { selectPhoto() }
        photo_img.setOnClickListener { selectPhoto() }
        back_button.setOnClickListener { finish() }
    }

    private fun signUpUser(name: String, email: String, password: String) {
        register_progress_bar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(Constraints.TAG, "createUserWithEmail:success")

                    val filename = UUID.randomUUID().toString()
                    val ref = storage.getReference("/images/${filename}")
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(selectedUri)
                        .build()

                    selectedUri?.let { uri ->
                        ref.putFile(uri).addOnSuccessListener {
                            Log.i("storage", uri.toString())

                            ref.downloadUrl.addOnSuccessListener {
                                Log.i("storage", it.toString())
                                val url = it.toString()
                                val uid = FirebaseAuth.getInstance().uid!!
                                val fbUser = User(uid, name, url)
                                FirebaseFirestore.getInstance().collection(USERS_DOC)
                                    .document(uid)
                                    .set(fbUser)
                                    .addOnFailureListener { Log.e("addUser", it.message, it) }
                            }
                        }
                    }

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Log.d(Constraints.TAG, "User profile updated.")
                                Toast.makeText(
                                    baseContext, R.string.successful_sign_up, Toast.LENGTH_SHORT
                                ).show()
                                register_progress_bar.visibility = View.GONE
                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constraints.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, R.string.failed_sign_up,
                        Toast.LENGTH_SHORT

                    ).show()
                    register_progress_bar.visibility = View.GONE
                }
            }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 20)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20) {
            selectedUri = data?.data
            Log.i("Teste", selectedUri.toString())
            selectedUri?.let{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                photo_img.setImageBitmap(bitmap)
                select_photo_button.visibility = View.GONE
            }
        }
    }
}