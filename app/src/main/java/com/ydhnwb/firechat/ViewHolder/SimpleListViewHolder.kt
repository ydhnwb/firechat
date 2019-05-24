package com.ydhnwb.firechat.ViewHolder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.ydhnwb.firechat.Interfaces.MyClickListener
import com.ydhnwb.firechat.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_item_daftar_chat.view.*

class SimpleListViewHolder(itemView : View, context : Context)  : RecyclerView.ViewHolder(itemView),
View.OnClickListener, View.OnLongClickListener{

    var displayName = itemView.findViewById<TextView>(R.id.list_display_name)
    var photoProfile = itemView.findViewById<CircleImageView>(R.id.list_photo)
    var onShortClickListener : MyClickListener? = null
    var onLongClickListener : MyClickListener? = null
    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        onShortClickListener?.onClick(v!!, adapterPosition, false)
    }

    override fun onLongClick(v: View?): Boolean {
        onShortClickListener?.onClick(v!!, adapterPosition, true)
        return false
    }


    fun setOnItemClickListener(c : MyClickListener){
        this.onShortClickListener = c
    }

    fun setOnLongItemClick(c : MyClickListener){
        this.onLongClickListener = c
    }

}