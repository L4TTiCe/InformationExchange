package io.github.l4ttice.informationexchange


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PostDetailsFragment : Fragment() {

    lateinit var PID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_post_details, container, false)

        PID = arguments!!["PID"] as String
        DBUtils.updatePostandAuthorDetailsPOST(view, PID)
        return view
    }


}
