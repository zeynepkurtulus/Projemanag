package com.sabanciuniv.projemanag.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sabanciuniv.projemanag.R
import kotlinx.android.synthetic.main.dialog_progress.*
// THIS ACTIVITY CONTAINS FUNCTIONS THAT EVERY OTHER ACTIVITY WILL USE
open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false // if the user presses the back button twice the activity should close
    // we want to display a progress dialog to the user each time something is loading
    private lateinit var mProgressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this )
        // set the screen content from a layout source the source will be inflated
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text = text
        // start the dialog and display it on the screen
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    // if the user presses the back button twice actually exit the activity
    fun doubleBackToExit(){
         if (doubleBackToExitPressedOnce){
             super.onBackPressed()
             return
         }

        // if the user clicks the back button once display this text
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT).show()

        // if the user has pressed only once then after a while we want to recent the count of the presses to the back button
        Handler().postDelayed({doubleBackToExitPressedOnce= false},  2000)
        // after 2000 miliseconds reset the button pressed info
    }

    fun showErrorSnackBar(message : String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view // need this to set an individual background color
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error_color))
        snackBar.show()
    }
}