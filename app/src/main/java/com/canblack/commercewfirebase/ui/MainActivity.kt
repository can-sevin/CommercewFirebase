package com.canblack.commercewfirebase.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.fragments.*
import com.dd.processbutton.iml.ActionProcessButton
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.lang.Exception
import com.google.firebase.database.DataSnapshot
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var user : FirebaseUser
    var storageProfilePicture = FirebaseStorage.getInstance().reference.child("Users Picture")
    lateinit var imageUri:Uri
    lateinit var myUrl:String
    private lateinit var uploadTask:UploadTask
    lateinit var downloadImageUrl:String
    var productData = FirebaseDatabase.getInstance().getReference("Products")
    var productImageRef: StorageReference = FirebaseStorage.getInstance().reference.child("Product Images")
    lateinit var productRef: DatabaseReference
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        productRef = FirebaseDatabase.getInstance().reference.child("Products")
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

    //data class Products(val name:String = "", val price:Double = 0.0, val image:String = "",val desc:String = "",val cat:String = "",val pid:String = "",val quantity:Int = 0)

    fun RecyclerNew(){
        val re_new = findViewById<RecyclerView>(R.id.recycler_new)

        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(productData,Products::class.java)
            .setLifecycleOwner(this)
            .build()


       val recyclerAdapter = object : FirebaseRecyclerAdapter<Products, ProductVH>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ProductVH {
                val homeRecyclerView = LayoutInflater.from(parent.context).inflate(R.layout.item_new,parent,false)
                return ProductVH(homeRecyclerView)
            }

            override fun onBindViewHolder(
                p0: ProductVH,
                p1: Int,
                p2: Products
            ) {
                p0.txtProductName.text = p2.name
                p0.txtProductDesc.text = p2.price.toString()+"TL"
                Picasso.get().load(p2.image).into(p0.imageView)
            }
        }
        re_new.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        re_new.adapter = recyclerAdapter
        re_new.setHasFixedSize(true)
        (recyclerAdapter as FirebaseRecyclerAdapter<Products, ProductVH>).startListening()
    }
    fun Login(email:String,pass:String){
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user = auth.currentUser!!
                    var adminRef = FirebaseDatabase.getInstance().getReference("Admin")
                    var userRef = FirebaseDatabase.getInstance().getReference("Users")
                    val progressLogin = ProgressDialog(this)
                    progressLogin.setTitle("Login on Process")
                    progressLogin.setMessage("Please wait")
                    progressLogin.setCancelable(false)
                    progressLogin.setIndeterminate(true)
                    progressLogin.setIndeterminateDrawable(resources.getDrawable(R.drawable.circular_back))
                    progressLogin.show()
                    userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            for (data in p0.children) {
                                val manager = supportFragmentManager
                                val transaction = manager.beginTransaction()
                                transaction.setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                if (data.child("email").value == email) {
                                    Thread.sleep(2000L)
                                    progressLogin.dismiss()
                                    transaction.replace(R.id.main_frame, HomeFragment(user)).addToBackStack(null).commit()
                                }
                            }
                    }
                })
                    adminRef.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            for (data in p0.children) {
                                val manager = supportFragmentManager
                                val transaction = manager.beginTransaction()
                                transaction.setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                if (data.child("email").value == email) {
                                    progressLogin.dismiss()
                                    transaction.replace(R.id.main_frame, AddProductFragment(user,auth)).addToBackStack(null).commit()
                                }
                            }
                        }
                    })
                }else{
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
                    val user = auth.currentUser
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    transaction.replace(R.id.main_frame, AddProductFragment(user!!,auth)).commit()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun AdminValidatePhone(name:String,tel:String,pass:String,email:String)
    {
        myRef = FirebaseDatabase.getInstance().reference
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val idemail = email.replace(".", ",")
                if(!(p0.child("Admin").child(idemail).exists())){
                    val userHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                    userHashMap.put("phone",tel)
                    userHashMap.put("password",pass)
                    userHashMap.put("name",name)
                    userHashMap.put("email",email)
                    myRef.child("Admin").child(idemail).updateChildren(userHashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful){
                                    Toast.makeText(baseContext, "Tebrikler Hesabınız oluşturuldu",
                                        Toast.LENGTH_SHORT).show()
                                    val manager = supportFragmentManager
                                    val transaction = manager.beginTransaction()
                                    transaction.setCustomAnimations(
                                        R.anim.fade_in,
                                        R.anim.fade_out
                                    )
                                    transaction.replace(R.id.main_frame, LoginFragment()).commit()
                                    val user = auth.currentUser
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
        myRef = FirebaseDatabase.getInstance().reference
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            val idemail = email.replace(".", ",")
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                    if(!(p0.child("Users").child(idemail).exists())){
                        val userHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                        userHashMap.put("phone",tel)
                        userHashMap.put("password",pass)
                        userHashMap.put("name",name)
                        userHashMap.put("email",email)
                        myRef.child("Users").child(idemail).updateChildren(userHashMap)
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
        galleryintent.action = Intent.ACTION_GET_CONTENT
        galleryintent.type = "image/*"
        startActivityForResult(galleryintent,1)
    }

    fun AddProduct(p_name:String,p_quantity:Int,p_desc:String,p_cat:String,p_price:Double,productKey:String){
        var filePath: StorageReference
        if(imageUri.toString().isEmpty()){
            Toast.makeText(baseContext, "You must install product image",
                Toast.LENGTH_SHORT).show()
        }else{
            filePath = productImageRef.child(imageUri.lastPathSegment+ productKey +".jpg")
            var uploadTask = filePath.putFile(imageUri)
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
                            downloadImageUrl = filePath.toString()
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
                                        productHashMap.put("image",p0.result.toString())
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


    fun uploadImageProfile(edtName: EditText?, edtEmail: EditText?, edtPass: EditText?, edtPhone: EditText?, img: CircleImageView?){
        val fileRef = storageProfilePicture.child(user.email.toString().replace(".",",")+".jpg")
        uploadTask = fileRef.putFile(imageUri)
        val manager = this.supportFragmentManager
        val transaction = manager.beginTransaction()

        uploadTask.continueWithTask(object : Continuation <UploadTask.TaskSnapshot, Task<Uri>>{

            override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                if(!p0.isSuccessful){
                    throw p0.exception!!
                }

                return fileRef.downloadUrl
            }
        })
            .addOnCompleteListener(object : OnCompleteListener<Uri> {

                override fun onComplete(p0: Task<Uri>) {
                    if(p0.isSuccessful){
                        var downloadUrl = p0.result
                        myUrl = downloadUrl.toString()

                        var userref = FirebaseDatabase.getInstance().reference.child("Users")

                        val userrHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                        userrHashMap.put("phone",edtPhone!!.text.toString())
                        userrHashMap.put("password",edtPass!!.text.toString())
                        userrHashMap.put("name",edtName!!.text.toString())
                        userrHashMap.put("email",edtEmail!!.text.toString())
                        userrHashMap.put("image",myUrl)
                        userref.child(user.email!!.replace(".",",")).updateChildren(userrHashMap)

                        transaction.setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        transaction.replace(R.id.main_frame, LoginFragment()).commit()
                        Toast.makeText(this@MainActivity, "Profile info updated success",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this@MainActivity, "Error info update",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    fun CropImage(){
        CropImage.activity(imageUri)
            .setAspectRatio(1,1)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data!=null){
            imageUri = data.data!!
            btn_add_img.text = "Image Added"
        }

        /*if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!=null){
            var result = CropImage.getActivityResult(data)
            imageUri = result.uri

            setting_profile_image.setImageURI(imageUri)
        } else {
            Toast.makeText(this, "Error,Try Again",
                Toast.LENGTH_SHORT).show()
            val manager = this.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, HomeFragment(user)).commit()
        }
         */
    }
}
