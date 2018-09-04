package io.github.l4ttice.informationexchange

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_post.*


class PostFragment : Fragment() {

    lateinit var databasePosts: DatabaseReference

    fun showSnackbar(id: String) {
        Snackbar.make(activity!!.findViewById(R.id.parent), id, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        val Button = view!!.findViewById<Button>(R.id.PostButton)
        Button.setOnClickListener {
            makePost()
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun makePost() {
        databasePosts = FirebaseDatabase.getInstance().getReference("post")
        val PID = databasePosts.push().key.toString()
        val mUser = FirebaseAuth.getInstance().currentUser
        val UID = mUser!!.uid
        val postTitle = titleID.text.toString()
        val postContent = contentID.text.toString()

        //val data = HashMap<String, kotlin.Any>()
        //data["UID"] = UID
        //data["title"] = postTitle
        //data["postContent"] = postContent
        //data["timestamp"] = ServerValue.TIMESTAMP

        val data = Post(postTitle, postContent, UID)
        databasePosts.child(PID).setValue(data)

        showSnackbar("Post Successful.")

    }
}
