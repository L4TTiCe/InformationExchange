package io.github.l4ttice.informationexchange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ViewFragment : Fragment() {

    private lateinit var Recycler: RecyclerView
    var viewAdapter: PostAdapterRecyclerView? = null
    var viewManager: LinearLayoutManager? = null
    lateinit var currentView: View
    var postList: ArrayList<Post?> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_view, container, false)

        viewAdapter = PostAdapterRecyclerView(ArrayList<Post?>())
        viewManager = LinearLayoutManager(activity)

        Recycler = currentView.findViewById<RecyclerView>(R.id.PostList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return currentView
    }

    override fun onStart() {
        super.onStart()

        DBUtils.databasePosts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w("VIEW_FRAG", "FETCHED DATA")
                postList.clear()
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = postSnapshot.getValue(Post::class.java)
                    postList.add(post)
                }
                viewAdapter!!.clear()
                Log.w("VIEW_FRAG", "" + postList.size + " # Added")
                viewAdapter!!.addData(postList)
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("VIEW_FRAG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

    }
}
