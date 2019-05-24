package com.ydhnwb.firechat

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ydhnwb.firechat.FirebaseHelper.FirebaseHelper

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()
        button_daftar.setOnClickListener {
            daftar()
        }
    }





    private fun daftar(){
        val name = daftar_nama.text.toString().trim()
        val email = daftar_email.text.toString().trim()
        val password = daftar_password.text.toString().trim()
        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length > 6){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    val me = mAuth.currentUser
                    if(me != null){
                        FirebaseHelper.pushUserData(name,email,me.uid)
                    }
                    Toast.makeText(this@RegisterActivity, "Register success", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@RegisterActivity, "Register failed", Toast.LENGTH_SHORT).show()
                }
            }
        }else{

            Toast.makeText(this@RegisterActivity, "Please fill all forms", Toast.LENGTH_SHORT).show()
        }
    }

}
