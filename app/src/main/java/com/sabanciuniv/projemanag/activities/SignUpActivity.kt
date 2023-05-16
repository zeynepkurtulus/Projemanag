package com.sabanciuniv.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sabanciuniv.projemanag.R
import com.sabanciuniv.projemanag.firebase.FirestoreClass
import com.sabanciuniv.projemanag.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign_up)
        //setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()


    }


    fun userRegisteredSuccess(){
        Toast.makeText(
            this,
            "You have successfully registered",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
        //startActivity(Intent(this,IntroActivity::class.java))
    }

    private fun setupActionBar(){

        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null){ // if the action bar exists
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)

        }

        toolbar_sign_up_activity.setNavigationOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        // how to cut out the empty space left at the end for example
        val name : String = et_name.text.toString().trim { it <= ' ' }
        val email : String = et_email.text.toString().trim{it <= ' '}
        val password : String = et_password.text.toString().trim { it<= ' ' }

        // checks if the inputs are correct then we want to register the user
        if (validateForm(name, email, password)){
            // we need to show the progress bar to the user to let them know that they are being registered
            showProgressDialog(resources.getString(R.string.please_wait)) // the function that we have in the base activity
            // firebase stuff
            // addOnCompleteListener will perform the stuff inside { } once its task is finished (once added to the firebase)
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful) { // ig the email and password is stored succesfully inside ethe firebase then we need to have a firebase user for tjat email and password
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    // created a new user for the firebase
                    val user = User(firebaseUser.uid,name,registeredEmail)
                    FirestoreClass().registerUser(this,user)
                }else{
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun validateForm(name:String, email:String, password:String): Boolean{

        // has the user entered something or not
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }

    }
}