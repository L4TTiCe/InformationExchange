package io.github.l4ttice.informationexchange

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import io.github.l4ttice.informationexchange.DBUtils.databasePosts
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment : Fragment() {

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

        val PID = databasePosts.push().key.toString()
        val mUser = FirebaseAuth.getInstance().currentUser
        val UID = mUser!!.uid
        val postTitle = titleID.text.toString()
        val postContent = contentID.text.toString()

        //DBUtils.checkNewUserv2(UID)

        //val data = HashMap<String, kotlin.Any>()
        //data["UID"] = UID
        //data["title"] = postTitle
        //data["postContent"] = postContent
        //data["timestamp"] = ServerValue.TIMESTAMP

        val data = Post(postTitle, postContent, UID)
        data.count = PostStats.wordcount(postContent)
        data.PID = PID

        data.value = (huffmanCode(postContent).encodedStringLength / 10) / data.count
        databasePosts.child(PID).setValue(data)

        Log.w("POST_FRAG", "POSTED")

        DBUtils.updateUser(UID, data.value)

        showSnackbar("Post Successful.")
        titleID.setText("")
        contentID.setText("")

    }
}
