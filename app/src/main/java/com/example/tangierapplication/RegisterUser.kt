package com.example.tangierapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
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
        if(email.text.toString().isEmpty())
        {
            email.error = "Please Enter an email ! "
            email.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
        {
            email.error="Please enter a valid email !"
            email.requestFocus()
            return
        }
        if(emailpass.text.toString().isEmpty())
        {
            emailpass.error = "Please enter a password !"
            emailpass.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(email.text.toString(),emailpass.text.toString()).addOnCompleteListener(this)
        {
            task -> if(task.isSuccessful)
        {
            val user = auth.currentUser
            user?.sendEmailVerification()
                ?.addOnCompleteListener { if(task.isSuccessful){
                    startActivity(Intent(this,MainActivity::class.java))
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