package com.ydhnwb.firechat

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.ydhnwb.firechat.Interfaces.MyClickListener
import com.ydhnwb.firechat.Model.UserModel
import com.ydhnwb.firechat.Utils.Constant
import com.ydhnwb.firechat.ViewHolder.SimpleListViewHolder

import kotlinx.android.synthetic.main.activity_new_chat.*
import kotlinx.android.synthetic.main.content_new_chat.*

class NewChatActivity : AppCompatActivity() {


    private lateinit var fireAdapter : FirebaseRecyclerAdapter<UserModel, SimpleListViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)
        setSupportActionBar(toolbar)

        val ml = LinearLayoutManager(this@NewChatActivity)
        rv_list_users.layoutManager = ml
        fetchData()

    }

    private fun getMyuid() : String{
        return intent.getStringExtra("MYUID") ?: throw IllegalArgumentException("MYUID is null") as Throwable
    }

    private fun fetchData(){
        val mRef = FirebaseDatabase.getInstance().getReference(Constant.USERS)
        val cRef = FirebaseDatabase.getInstance().getReference(Constant.CHAT)

        val fo = FirebaseRecyclerOptions.Builder<UserModel>().setQuery(
                mRef.orderByChild("name"), UserModel::class.java).build()

        fireAdapter = object : FirebaseRecyclerAdapter<UserModel, SimpleListViewHolder>(fo){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleListViewHolder {
                return SimpleListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_daftar_chat,
                        parent, false),this@NewChatActivity)
            }

            override fun onBindViewHolder(holder: SimpleListViewHolder, position: Int, model: UserModel) {
                holder.displayName.text = model.name
                Glide.with(this@NewChatActivity).load(model.photoUrl).into(holder.photoProfile)
                holder.setOnItemClickListener(object : MyClickListener{
                    override fun onClick(v: View, position: Int, isLongClick: Boolean) {
                        if(model.uid.equals(getMyuid())){
                            Toast.makeText(this@NewChatActivity, "You are clicking yourself", Toast.LENGTH_SHORT).show()
                        }else{

                            val i = Intent(this@NewChatActivity, ConversationActivity::class.java)
                            i.putExtra("MYUID", getMyuid())
                            i.putExtra("TARGETUID", model.uid)
                            startActivity(i)
                        }
                    }

                })
            }

        }

        rv_list_users.adapter = fireAdapter
        fireAdapter.startListening()

    }

}
