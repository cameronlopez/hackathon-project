package com.example.visahackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home.*

class Login : AppCompatActivity() {

    lateinit var mEmail : EditText
    lateinit var mPassword : EditText
    lateinit var mRegisterBtn : TextView
    lateinit var mLoginBtn : Button
    lateinit var fAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password)
        mRegisterBtn = findViewById(R.id.not_registered_button)
        mLoginBtn = findViewById(R.id.login_button)

        fAuth = FirebaseAuth.getInstance()

        if(fAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mLoginBtn.setOnClickListener {

            // validate user
            val email : String = mEmail.text.toString().trim()
            val password : String = mPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                mEmail.error = "Email is Required."
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.error = "Password is Required."
                return@setOnClickListener
            }

            //register to database
            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Log.d("Main", "Failed to login user: ${it.message}")
            }
        }

        mRegisterBtn.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }
}
