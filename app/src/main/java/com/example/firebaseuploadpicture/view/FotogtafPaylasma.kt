package com.example.firebaseuploadpicture.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebaseuploadpicture.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotogtaf_paylasma.*
import java.util.UUID

class FotogtafPaylasma : AppCompatActivity() {
    var secilenGorsel: Uri?=null
    var secilenBitmap:Bitmap?=null
    private lateinit var storage:FirebaseStorage
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotogtaf_paylasma)
        auth=FirebaseAuth.getInstance()
        storage=FirebaseStorage.getInstance()
        database=FirebaseFirestore.getInstance()
    }
    fun paylas(view: View)
    {
        var uuid=UUID.randomUUID()
        var gorselismi="${uuid}.jpg"
        var reference=storage.reference
        var gorselRefernce=reference.child("images").child(gorselismi)
        if (secilenGorsel!=null)
        {
            //addOnSuccessListener metodu bize görsel yüklemenin basarili oldugundan sonra
            //veritabanına
            gorselRefernce.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot->

                //yuklenenGorselReference ile yüklediğimiz görselin uri adresini alıyoruz.
               val yuklenenGorselReference=FirebaseStorage.getInstance().reference.child("images").child(gorselismi)
                //bu görselin uri adresini aldık.şimdi ise veritabanına kaydetmeye geldi.
                //fireabsefirestore e kaydedecez.
                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri->
                 var downloadUrl=uri.toString()
                 var guncelKullaniciEmaili=auth.currentUser!!.email.toString()
                 var kullaniciismim=nameText.text.toString()
                 var tarih=Timestamp.now()
                 var dogumTarihi= dogumText.text.toString()
                 var tc=tcText.text.toString()

                 var postHashMap= hashMapOf<String,Any>()
                 postHashMap.put("gorselurl",downloadUrl)
                 postHashMap.put("kullaniciyorum",guncelKullaniciEmaili)
                 postHashMap.put("kullanicaadi",kullaniciismim)
                 postHashMap.put("sistemekayıtoluştarih",tarih)
                 postHashMap.put("dogumtarihi",dogumTarihi)
                 postHashMap.put("tc",tc)
                    database.collection("Kto").add(postHashMap).addOnCompleteListener { task->
                     if (task.isSuccessful){
                      finish()
                     }
                    }.addOnFailureListener { exception->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception->
                    Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    fun gorselSec(view: View)
    {
        //READ_EXTERNAL_STORAGE permissionu manifest in içine ekliyoruz. ve buarada kontrol ediyoruz
        //eger permission eklenmemiş ise kullanıcıdan izin istemek için if içine girer.
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            //izin alamadık
            //ActivityCompat içinde array kullanmamızın sebebi kullanıcıdan birden fazla izin isteyebiliriz o yüzdendeir.
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else
        {
            //izin verilmis ve galeriye gönderiyoruz.
            var galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }

    //onRequestPermissionsResult verdiğimiz izinlerin sonucunda ne olacak onun için kullanırız.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1)
        {
            if (grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {//izin verildiginde yapılacakalr
                var galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      //sdInt>=28 yapmamızın sebebi imageDecoder kullanmamız için kontrol ediyoruz.
        //çünkü 28 den sonra gelen bir özellik oldugu için
       //data.data ile görselin konumunu alıyoruz.
        secilenGorsel=data?.data
       if (secilenGorsel !=null){
        if(Build.VERSION.SDK_INT>=28)
        {
            //Image Decoder; Android’de İçerik Sağlayıcı, uygulamanın verilerini tek bir yerde depolamak
            // ve bu verileri gerektiğinde farklı uygulamaların erişmesi için kullanılabilir hale getirmek
            // için merkezi bir havuz görevi görecektir.
        var source=ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
        secilenBitmap=ImageDecoder.decodeBitmap(source)
        imageView.setImageBitmap(secilenBitmap)
        }
       }//kullanıcı belki galeriye girip bişey secmeyebilir.Bunu kontrol etmek için else blogu var en sondaki veriyi getirir
       else{
           secilenBitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
           imageView.setImageBitmap(secilenBitmap)
       }
        super.onActivityResult(requestCode, resultCode, data)
    }
}




