package io.github.l4ttice.informationexchange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.post_listview.view.*

class PostAdapterRecyclerView(private val Dataset: ArrayList<Post?>) : RecyclerView.Adapter<PostAdapterRecyclerView.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var Heading: TextView = view.item_head
        var PostContent: ExpandableTextView = view.item_content
        var status: TextView = view.item_status
        var value: TextView = view.tokenview

        fun bindDetails(PID: String) {
            view.setOnClickListener {
                var intent: Intent = Intent(view.context, PostDetailsActivity::class.java)
                intent.putExtra("PID", PID)
                startActivity(view.context, intent, Bundle())
            }
        }
    }

    fun clear() {
        val size = Dataset.size
        Log.w("RECYCL", "START CLEAR SIZE=" + size)
        if (size == 0) {
            return
        }
        for (i in 0 until size) {
            try {
                //Log.w("RECYCL", "REMOVED DATA #"+i)
                Dataset.removeAt((size - 1) - i)
                notifyDataSetChanged()
            } catch (In: IndexOutOfBoundsException) {
                Log.e("RECYCL", "Index Out of Bounds")
            }

        }
    }

    override fun getItemCount(): Int {
        return Dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_listview, parent, false)
        context = parent.context
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = Dataset[position]
        holder.Heading.text = currentData!!.title
        holder.PostContent.text = currentData.contentPost
        holder.value.text = currentData.value.toString()
        holder.status.text = "Purchased"
        holder.bindDetails(currentData.PID)
    }

    fun addData(result: ArrayList<Post?>?) {
        if (result == null) {
            return
        }
        val currentSize = Dataset.size
        for (i in 0 until result.size) {
            Dataset.add(result[i])
            //Log.w("RECYCL", "Added Data #"+i)
        }
        notifyItemRangeInserted(currentSize, result.size)
        Log.w("RECYCL", "NOTIFIED CHANGE")
    }
}