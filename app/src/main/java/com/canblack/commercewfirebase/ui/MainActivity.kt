package com.canblack.commercewfirebase.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.widget.Toast
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.fragments.AddProductFragment
import com.canblack.commercewfirebase.ui.fragments.LoginFragment
import com.google.android.gms.tasks.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ImageUri:Uri
    private lateinit var filePath: StorageReference
    private lateinit var productRef: DatabaseReference
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        productRef = FirebaseDatabase.getInstance().getReference().child("Products")
        val currentUser = auth.currentUser

        if(savedInstanceState == null){
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.add(R.id.main_frame, LoginFragment()).commit()
        }
    }

    override fun onStop() {
        super.onStop()
        auth.signOut()
    }

    fun Login(email:String,pass:String){
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun Register(email:String,pass:String,name:String,tel:String){
        ValidatePhone(name,tel,pass,email)
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun AdminRegister(email:String,pass:String,name:String,tel:String){
        AdminValidatePhone(name,tel,pass,email)
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    transaction.add(R.id.main_frame, AddProductFragment()).commit()
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun AdminValidatePhone(name:String,tel:String,pass:String,email:String)
    {
        myRef = FirebaseDatabase.getInstance().getReference()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(!(p0.child("Admin").child("phone").exists()) && !(p0.child("Admin").child("email").exists())){
                    val userHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                    userHashMap.put("phone",tel)
                    userHashMap.put("password",pass)
                    userHashMap.put("name",name)
                    userHashMap.put("email",email)
                    myRef.child("Admin").child("phone").updateChildren(userHashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful){
                                    Toast.makeText(baseContext, "Tebrikler Hesabınız oluşturuldu",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                } else {
                    Toast.makeText(baseContext, "This phone number in our records.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun ValidatePhone(name:String,tel:String,pass:String,email:String)
    {
        myRef = FirebaseDatabase.getInstance().getReference()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                    if(!(p0.child("Users").child("phone").exists()) && !(p0.child("Users").child("email").exists())){
                        val userHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                        userHashMap.put("phone",tel)
                        userHashMap.put("password",pass)
                        userHashMap.put("name",name)
                        userHashMap.put("email",email)
                        myRef.child("Users").child("phone").updateChildren(userHashMap)
                            .addOnCompleteListener(object : OnCompleteListener<Void>{
                                override fun onComplete(p0: Task<Void>) {
                                    if(p0.isSuccessful){
                                        Toast.makeText(baseContext, "Tebrikler Hesabınız oluşturuldu",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                    } else {
                        Toast.makeText(baseContext, "This phone number in our records.",
                            Toast.LENGTH_SHORT).show()
                    }
            }
        })
    }
    fun Forgot(email:String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Check your email for password reset link.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Didn't match e-mail our records",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun OpenGallery(){
        val galleryintent = Intent()
        galleryintent.setAction(Intent.ACTION_GET_CONTENT)
        galleryintent.setType("image/*")
        startActivityForResult(galleryintent,1)
    }

    fun AddProduct(p_name:String,p_quantity:Int,p_desc:String,p_cat:String,p_price:Float,productKey:String){
        if(ImageUri.toString().isEmpty()){
            Toast.makeText(baseContext, "You must install product image",
                Toast.LENGTH_SHORT).show()
        }else{
            filePath = FirebaseStorage.getInstance().getReference().child("Product Images")
                .child(ImageUri.lastPathSegment + productKey+".jpg")

            val uploadTask = filePath.putFile(ImageUri)

            uploadTask.addOnFailureListener(object:OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Toast.makeText(baseContext, "Image install process is failure",
                        Toast.LENGTH_SHORT).show()
                }
            }).addOnSuccessListener(object:OnSuccessListener<UploadTask.TaskSnapshot>{
                override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                    Toast.makeText(baseContext, "Image install process is success",
                        Toast.LENGTH_SHORT).show()

                    val urlTask = uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                        override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                            if(!p0.isSuccessful){
                                throw p0.exception!!
                            }

                            val downloadImageUrl = filePath.downloadUrl.toString()
                            return filePath.downloadUrl
                        }
                    }).addOnCompleteListener(object : OnCompleteListener<Uri>{
                        override fun onComplete(p0: Task<Uri>) {
                               if(p0.isSuccessful){
                                   Toast.makeText(baseContext, "Product Image save to Database",
                                       Toast.LENGTH_SHORT).show()
                                        val productHashMap = HashMap<String,Any>()
                                        productHashMap.put("pid",productKey)
                                        productHashMap.put("name",p_name)
                                        productHashMap.put("desc",p_desc)
                                        productHashMap.put("cat",p_cat)
                                        productHashMap.put("quantity",p_quantity)
                                        productHashMap.put("price",p_price)

                                   productRef.child(productKey).updateChildren(productHashMap)
                                       .addOnCompleteListener(object : OnCompleteListener<Void>{
                                           override fun onComplete(p0: Task<Void>) {
                                               if(p0.isSuccessful){
                                                   Toast.makeText(baseContext, "Product is added",
                                                       Toast.LENGTH_SHORT).show()
                                               } else {
                                                   Toast.makeText(baseContext, p0.exception.toString(),
                                                       Toast.LENGTH_SHORT).show()
                                               }
                                           }
                                   })
                               }
                        }
                    })
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data!=null){
            ImageUri = data.data!!
            btn_add.setText("Image Added")
        }
    }
}
