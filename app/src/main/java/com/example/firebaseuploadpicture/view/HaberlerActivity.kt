package com.example.firebaseuploadpicture.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseuploadpicture.model.Post
import com.example.firebaseuploadpicture.R
import com.example.firebaseuploadpicture.adapter.HaberRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var database :FirebaseFirestore
    private  lateinit var recyclerAdapter: HaberRecyclerAdapter

    var postistesi=ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)
        auth=FirebaseAuth.getInstance()
        database=FirebaseFirestore.getInstance()

        verileriAl()
        var layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerAdapter=HaberRecyclerAdapter(postistesi)
        recyclerView.adapter=recyclerAdapter
    }
        fun  verileriAl()
        {
            database.collection("Kto").orderBy("sistemekayıtoluştarih", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
                if (error!=null)
                {
                 Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
                }
                else
                {
                    //degerlerin null olmadıgına ve boş olmadıgına emin olalaım
                    if (value!=null)
                    {
                        if (!value.isEmpty)
                        {
                           var documents= value.documents
                            for (document in documents)
                            {
                              var kullanicimi = document.get("kullanicaadi") as String
                              var kullaniciEmaili = document.get("kullaniciyorum") as String
                              var gorselUrl = document.get("gorselurl") as String
                              val indirilenPost= Post(kullaniciEmaili, kullanicimi ,gorselUrl)
                                postistesi.add(indirilenPost)
                            }
                            recyclerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater=menuInflater
        menuInflater.inflate(R.menu.seceneklermenusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.fotograf_paylas)
        {
            var intent= Intent(this, FotogtafPaylasma::class.java)
            startActivity(intent)
        }
        else if(item.itemId== R.id.cikisyap)
        {
            //fireabse den çıkıs yapıyor.
            auth.signOut()
            var intent1= Intent(this, KullanicisActivity::class.java)
            startActivity(intent1)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}