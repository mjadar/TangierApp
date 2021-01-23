package com.example.tangierapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.tangierapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_form.*

class RegisterUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)
        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            signUpButton()
        }


    }
    fun signUpButton()
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
        auth.createUserWithEmailAndPassword(emailRegister.text.toString(),emailPassR.text.toString()).addOnCompleteListener(this)
        {
            task -> if(task.isSuccessful)
        {
            val user = auth.currentUser
            user?.sendEmailVerification()
                ?.addOnCompleteListener { if(task.isSuccessful){
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
}