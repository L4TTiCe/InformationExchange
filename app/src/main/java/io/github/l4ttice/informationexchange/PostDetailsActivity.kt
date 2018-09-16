package io.github.l4ttice.informationexchange

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_postdetails.*

class PostDetailsActivity : AppCompatActivity() {

    lateinit var PID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postdetails)

        val ID: String = intent.getStringExtra("PID")
        PID = ID
        DBUtils.updatePostDetailsUI(parentview, PID)

        // Find the view pager that will allow the user to swipe between fragments
        val pager: ViewPager = findViewById(R.id.viewpager)
        val tabBar: TabLayout = findViewById(R.id.sliding_tabs)

        // Create an adapter that knows which fragment should be shown on each page
        val DetailsAdapter = PostDetailsFragmentPagerAdapter(supportFragmentManager, PID)

        // Set the adapter onto the view pager
        pager.adapter = DetailsAdapter
        tabBar.setupWithViewPager(pager)

        purchaseButton.setOnClickListener {
            DBUtils.ProcessPurchase(DBUtils.fetchUID(), PID, this.findViewById<View>(android.R.id.content).getRootView())
        }

        cancelButton.setOnClickListener {
            finish()
        }

        DBUtils.PostDetailsUIHandler(this.findViewById<View>(android.R.id.content).getRootView(), PID)
    }

}
