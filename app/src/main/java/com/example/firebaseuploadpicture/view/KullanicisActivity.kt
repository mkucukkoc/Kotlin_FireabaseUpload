package com.example.firebaseuploadpicture.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebaseuploadpicture.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_kullanicis.*

class KullanicisActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanicis)

        auth=FirebaseAuth.getInstance()
        var guncelKullanicis=auth.currentUser?.email
        if(guncelKullanicis != null)
        {
            var intent= Intent(this, HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun login(view: View)
    {
        auth.signInWithEmailAndPassword(emailText.text.toString(),sifreText.text.toString()).addOnCompleteListener{ task->

            if(task.isSuccessful)
            {
                var guncelKullanici=auth.currentUser?.email.toString()
                Toast.makeText(this, "Giriş Başarılı!Hoşgeldin ${guncelKullanici}", Toast.LENGTH_LONG).show()
                var intent= Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception->
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    fun register(view: View)
    {
        var intent= Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()

    }
}