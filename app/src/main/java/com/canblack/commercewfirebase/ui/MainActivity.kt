package com.canblack.commercewfirebase.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.fragments.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_product.*
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var user : FirebaseUser
    private var storageProfilePicture = FirebaseStorage.getInstance().reference.child("Users Picture")
    private lateinit var imageUri:Uri
    private lateinit var myUrl:String
    private lateinit var uploadTask:UploadTask
    private lateinit var downloadImageUrl:String
    private var productData = FirebaseDatabase.getInstance().getReference("Products")
    private var productImageRef: StorageReference = FirebaseStorage.getInstance().reference.child("Product Images")
    private lateinit var productRef: DatabaseReference
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        productRef = FirebaseDatabase.getInstance().reference.child("Products")
        if(savedInstanceState == null){
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.add(R.id.main_frame, LoginFragment(),"Login").commit()
        }
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.fragments.last().tag == "Login" -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout?")
                builder.setMessage("Are you want to exit?")
                builder.setPositiveButton("YES"){ _, _ ->
                    auth.signOut()
                    finish()
                }
                builder.setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            supportFragmentManager.fragments.last().tag == "Home" -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout?")
                builder.setMessage("Are you want to logout?")
                builder.setPositiveButton("YES"){ _ , _ ->
                    auth.signOut()
                    finish()
                }
                builder.setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            supportFragmentManager.fragments.last().tag == "AdminHome" -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout?")
                builder.setMessage("Are you want to logout?")
                builder.setPositiveButton("YES"){ _, _ ->
                    auth.signOut()
                    finish()
                }
                builder.setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            supportFragmentManager.fragments.last().tag == "forgot" -> {
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.add(R.id.main_frame, LoginFragment(),"Login").commit()
            }
            supportFragmentManager.fragments.last().tag == "homeback" -> {
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.add(R.id.main_frame, HomeFragment(user),"Home").commit()
            }
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
                    val adminRef = FirebaseDatabase.getInstance().getReference("Admin")
                    val userRef = FirebaseDatabase.getInstance().getReference("Users")
                    progressLogin.isIndeterminate = true
                    progressLogin.visibility = View.VISIBLE
                    userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(baseContext, p0.toString(),
                                Toast.LENGTH_SHORT).show()
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
                                    progressLogin.visibility = View.GONE
                                    transaction.replace(R.id.main_frame, HomeFragment(user),"Home").commit()
                                }
                            }
                    }
                })
                    adminRef.addListenerForSingleValueEvent(object:ValueEventListener{
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
                                    progressLogin.visibility = View.GONE
                                    transaction.replace(R.id.main_frame, AddProductFragment(user,auth),"AdminHome").commit()
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
                if (!task.isSuccessful) {
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
                    transaction.replace(R.id.main_frame, AddProductFragment(user!!,auth),"AdminHome").commit()
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
                Toast.makeText(baseContext, p0.toString(),
                    Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                val idemail = email.replace(".", ",")
                if(!(p0.child("Admin").child(idemail).exists())){
                    val userHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                    //Android KTX
                    userHashMap["phone"] = tel
                    userHashMap["password"] = pass
                    userHashMap["name"] = name
                    userHashMap["email"] = email
                    myRef.child("Admin").child(idemail).updateChildren(userHashMap)
                        .addOnCompleteListener { p0 ->
                            if(p0.isSuccessful){
                                Toast.makeText(baseContext, "Tebrikler Hesabınız oluşturuldu",
                                    Toast.LENGTH_SHORT).show()
                                val manager = supportFragmentManager
                                val transaction = manager.beginTransaction()
                                transaction.setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                transaction.replace(R.id.main_frame, LoginFragment(),"Login").commit()
                            }
                        }
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
                Toast.makeText(baseContext, p0.toString(),
                    Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                    if(!(p0.child("Users").child(idemail).exists())){
                        val userHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                        userHashMap.put("phone",tel)
                        userHashMap.put("password",pass)
                        userHashMap.put("name",name)
                        userHashMap.put("email",email)
                        myRef.child("Users").child(idemail).updateChildren(userHashMap)
                            .addOnCompleteListener { p0 ->
                                if(p0.isSuccessful){
                                    Toast.makeText(baseContext, "Tebrikler Hesabınız oluşturuldu",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
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
        val filePath: StorageReference
        if(imageUri.toString().isEmpty()){
            Toast.makeText(baseContext, "You must install product image",
                Toast.LENGTH_SHORT).show()
        }else{
            filePath = productImageRef.child(imageUri.lastPathSegment+ productKey +".jpg")
            val uploadTask = filePath.putFile(imageUri)
            uploadTask.addOnFailureListener {
                Toast.makeText(baseContext, "Image install process is failure",
                    Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(baseContext, "Image install process is success",
                    Toast.LENGTH_SHORT).show()
                val urlTask = uploadTask.continueWithTask { p0 ->
                    if(!p0.isSuccessful){
                        throw p0.exception!!
                    }
                    downloadImageUrl = filePath.toString()
                    filePath.downloadUrl
                }.addOnCompleteListener { p0 ->
                    if(p0.isSuccessful){
                        Toast.makeText(baseContext, "Product Image save to Database",
                            Toast.LENGTH_SHORT).show()
                        val productHashMap = HashMap<String,Any>()
                        //Android KTX
                        productHashMap["pid"] = productKey
                        productHashMap["name"] = p_name
                        productHashMap["desc"] = p_desc
                        productHashMap["image"] = p0.result.toString()
                        productHashMap["cat"] = p_cat
                        productHashMap["quantity"] = p_quantity
                        productHashMap["price"] = p_price

                        productRef.child(productKey).updateChildren(productHashMap)
                            .addOnCompleteListener { p0 ->
                                if(p0.isSuccessful){
                                    Toast.makeText(baseContext, "Product is added",
                                        Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(baseContext, p0.exception.toString(),
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }


    fun uploadImageProfile(edtName: EditText?, edtEmail: EditText?, edtPass: EditText?, edtPhone: EditText?, img: CircleImageView?){
        val fileRef = storageProfilePicture.child(user.email.toString().replace(".",",")+".jpg")
        uploadTask = fileRef.putFile(imageUri)
        val manager = this.supportFragmentManager
        val transaction = manager.beginTransaction()

        uploadTask.continueWithTask { p0 ->
            if(!p0.isSuccessful){
                throw p0.exception!!
            }

            fileRef.downloadUrl
        }
            .addOnCompleteListener { p0 ->
                if(p0.isSuccessful){
                    val downloadUrl = p0.result
                    myUrl = downloadUrl.toString()

                    val userref = FirebaseDatabase.getInstance().reference.child("Users")

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
                    transaction.replace(R.id.main_frame, LoginFragment(),"Login").commit()
                    Toast.makeText(this@MainActivity, "Profile info updated success",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@MainActivity, "Error info update",
                        Toast.LENGTH_SHORT).show()
                }
            }
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
