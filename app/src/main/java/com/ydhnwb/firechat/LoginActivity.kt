package com.ydhnwb.firechat

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        initAuth()
    }

    private fun initAuth(){
        val mAuth = FirebaseAuth.getInstance()
        mAuth.addAuthStateListener {oy ->
            val me = oy.currentUser
            if(me != null){
                val i = Intent(this@LoginActivity, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }else{
                login()
                daftar.setOnClickListener{
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }
        }
    }


    private fun login(){
        button_login.setOnClickListener {
            val email = login_email.text.toString().trim()
            val password = login_password.text.toString().trim()
            if(!email.isEmpty() && !password.isEmpty() && password.length > 6){
                val mAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Failed login", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
