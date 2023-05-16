package com.sabanciuniv.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.sabanciuniv.projemanag.R
import com.sabanciuniv.projemanag.firebase.FirestoreClass
import com.sabanciuniv.projemanag.models.User
import com.sabanciuniv.projemanag.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{
    companion object {// contain info about our profile request code
        const val MY_PROFILE_REQUEST_CODE : Int= 11
    }

    // this variable type indicates that this object will be initialized later on
    private lateinit var mUserName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpActionBar()
        nav_view.setNavigationItemSelectedListener(this)
        FirestoreClass().loadUserData(this) // this calls  updateNavigationUserDetails in which the username is set that is why we can access the username
        fab_create_board.setOnClickListener{
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName) // at constants in the place of name I want to pass the username
            startActivity(intent) // we are sending the username to the CreateBoardActivity and we have to catch it inside that activity
        }
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        // if the drawer is open then
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    //load the image of the logged in user which inside the nav_header_main
    fun updateNavigationUserDetails(user: User){
        // set the user image from the db
        mUserName = user.name
        Glide // an external library
            .with(this) // us eit in this activity
            .load(user.image ) // load th user image
            .centerCrop() // place it in the center
            .placeholder(R.drawable.ic_user_place_holder) // the place to load the user image
            .into(nav_user_image); // into this

        // set the actual name of the user from the db
        tv_username.text = user.name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            // if these codes are given i.e. no error we need to load the user data into the side bar
            FirestoreClass().loadUserData(this)
        }else{ // if there occurs an error
            Log.e("Cancelled", "Cancelled")
        }
    }
    // the functionalities when the profile or the other icon is clicked
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out->{
                FirebaseAuth.getInstance().signOut()
                //Toast.makeText(this,"Sign Out is clicked", Toast.LENGTH_LONG).show()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}