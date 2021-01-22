package com.example.tangierapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.email
import kotlinx.android.synthetic.main.activity_main.emailpass
import kotlinx.android.synthetic.main.activity_register_form.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {


        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerbutton.setOnClickListener {
            startActivity(Intent(this,RegisterUser::class.java))
            finish()
        }

        signin.setOnClickListener {
            doLogin()
        }

    }

    public override fun onStart()
    {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    fun doLogin()
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
        auth.signInWithEmailAndPassword(email.text.toString(),emailpass.text.toString()).addOnCompleteListener(this)
        {
            task-> if(task.isSuccessful)
        {
            val user = auth.currentUser
            updateUI(user)
        }
            else
        {
            Toast.makeText(baseContext,"Login Failed !", Toast.LENGTH_SHORT).show()
            updateUI(null)

        }
        }
    }
    private fun updateUI(currentUser: FirebaseUser?)
    {
          if(currentUser != null)
          {
              if(currentUser.isEmailVerified){
                  startActivity(Intent(this,HomePage::class.java))
                  finish()
              }
              else
              {
                  Toast.makeText(baseContext,"Please verify your email adress !", Toast.LENGTH_SHORT).show()
              }
          }
        else
          {
              Toast.makeText(baseContext,"Login Failed !", Toast.LENGTH_SHORT).show()
          }
    }
}