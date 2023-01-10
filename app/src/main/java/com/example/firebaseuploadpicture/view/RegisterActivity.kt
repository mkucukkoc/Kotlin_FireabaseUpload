package com.example.firebaseuploadpicture.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebaseuploadpicture.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth=FirebaseAuth.getInstance()

    }
    fun backhome(view: View)
    {
        var intent= Intent(this, KullanicisActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun register(view: View)
    {
        var email=emailText.text.toString()
        var sifre=passwordText.text.toString()

        if (email==""&&sifre=="")
        {
            Toast.makeText(applicationContext,"Lütfen kayıt olmak için alanları doldurunuz",Toast.LENGTH_LONG).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"Kaydınız Başarıyla Oluşturuldu.Giriş Ekranına Yönlendiriliyorsunuz...",Toast.LENGTH_LONG).show()

                    var intent = Intent(this, KullanicisActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }
}