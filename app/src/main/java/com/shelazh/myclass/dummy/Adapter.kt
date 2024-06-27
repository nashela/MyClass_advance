package com.shelazh.myclass.dummy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shelazh.myclass.DetailFriendActivity
import com.shelazh.myclass.R

class Adapter(private val context: Context) : RecyclerView.Adapter<Adapter.FriendViewHolder>() {

    private val dummyData = List(10) { "Friend ${it + 1}" } // Data dummy

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.FriendViewHolder, position: Int) {
        holder.bind(dummyData[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailFriendActivity::class.java)
            intent.putExtra("name", dummyData[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dummyData.size

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.tv_name)

        fun bind(name: String) {
            textView.text = name
        }
    }
}
