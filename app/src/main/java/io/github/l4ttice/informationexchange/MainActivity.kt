package io.github.l4ttice.informationexchange

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123 //the request code could be any Integer
    val auth = FirebaseAuth.getInstance()!!

    fun showSnackbar(id : Int){
        Snackbar.make(findViewById(R.id.parent), resources.getString(id), Snackbar.LENGTH_LONG).show()
    }

    fun showSnackbar(id : String){
        Snackbar.make(findViewById(R.id.parent), id, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(auth.currentUser != null){ //If user is signed in
//                startActivity(Next Activity)
            showSnackbar("Already Logged in.")
        }
        else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setAvailableProviders(Arrays.asList(
                                    AuthUI.IdpConfig.GoogleBuilder().build(),
                                    AuthUI.IdpConfig.EmailBuilder().build()))
                            .setTosAndPrivacyPolicyUrls(
                                    "https://superapp.example.com/terms-of-service.html",
                                    "https://superapp.example.com/privacy-policy.html")
                            .build(),
                    RC_SIGN_IN)
        }

        // Find the view pager that will allow the user to swipe between fragments
        val pager: ViewPager = findViewById(R.id.viewpager)
        val tabBar: TabLayout = findViewById(R.id.sliding_tabs)

        // Create an adapter that knows which fragment should be shown on each page
        val PostAdapter = PostFragmentPagerAdapter(supportFragmentManager)

        // Set the adapter onto the view pager
        pager.adapter = PostAdapter
        tabBar.setupWithViewPager(pager)

    }
}