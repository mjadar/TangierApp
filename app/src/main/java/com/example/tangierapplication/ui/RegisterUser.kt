package com.example.tangierapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Saved
import com.example.tangierapplication.models.User
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.rpc.context.AttributeContext
import kotlinx.android.synthetic.main.activity_register_form.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class RegisterUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)
        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            signUp()
        }


    }
    fun signUp()
    {
        if(emailRegister.text.toString().isEmpty())
        {
            emailRegister.error = "Please Enter an email ! "
            emailRegister.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailRegister.text.toString()).matches())
        {
            emailRegister.error="Please enter a valid email !"
            emailRegister.requestFocus()
            return
        }
        if(emailPassR.text.toString().isEmpty())
        {
            emailPassR.error = "Please enter a password !"
            emailPassR.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(emailRegister.text.toString(),emailPassR.text.toString())
            .addOnCompleteListener(this) {
                task -> if(task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            if(task.isSuccessful){
                                var profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameRegister.text.toString())
                                        .build()
                                user.updateProfile(profileUpdates)
                                val instance = User(emailRegister.text.toString(),nameRegister.text.toString(),user.uid)
                                addUserToFirestore(instance)

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                }
            else
            {
                Toast.makeText(baseContext,"Sign up Failed !",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun addUserToFirestore(userInstance : User){
        val userRef =   db.collection("Users").document(auth.currentUser?.uid!!)
        // In a transaction, add the new rating and update the aggregate totals
        Firebase.firestore.runTransaction { transaction ->
            // Commit to Firestore
            transaction.set(userRef,userInstance)
            null
        } .addOnSuccessListener {
            Log.d("MainActivity", "User Added Successfully")
        }.addOnFailureListener { e ->
            Log.w("MainAcitivity", "Add User failed", e)
        }
    }
}