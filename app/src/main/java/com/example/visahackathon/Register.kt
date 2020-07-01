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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.home.*
import java.util.*

class Register : AppCompatActivity() {

    lateinit var mFullName : EditText
    lateinit var mEmail : EditText
    lateinit var mPassword : EditText
    lateinit var mRegisterBtn : Button
    lateinit var mLoginBtn : TextView
    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        mFullName = findViewById(R.id.full_name)
        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password)
        mRegisterBtn = findViewById(R.id.register_button)
        mLoginBtn = findViewById(R.id.already_registered_button)

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        if(fAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mRegisterBtn.setOnClickListener {

            // validate user
            val fullName : String = mFullName.text.toString().trim()
            val email : String = mEmail.text.toString().trim()
            val password : String = mPassword.text.toString().trim()

            if (TextUtils.isEmpty(fullName)) {
                mFullName.error = "Full Name is Required."
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                mEmail.error = "Email is Required."
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.error = "Password is Required."
                return@setOnClickListener
            }

            //register to database
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                val uuid = UUID.randomUUID().toString()

                val user = User(uuid, email, password, fullName, "")

                fDatabase.reference.child("/users/$uuid").setValue(user)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")

                if(it.message.toString().contains("email"))
                    mEmail.error = it.message.toString()
                else
                    mPassword.error = it.message.toString()
            }
        }

        mLoginBtn.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}
