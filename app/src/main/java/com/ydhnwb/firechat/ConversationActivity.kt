package com.ydhnwb.firechat

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.ydhnwb.firechat.Adapter.MessageListAdapter
import com.ydhnwb.firechat.Model.ChatModelRetriever
import com.ydhnwb.firechat.Model.ChatModelUploader
import com.ydhnwb.firechat.Model.UserModel
import com.ydhnwb.firechat.Utils.Constant

import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.bottom_bar_chat.*
import kotlinx.android.synthetic.main.content_conversation.*

class ConversationActivity : AppCompatActivity() {

    private var chatRef = FirebaseDatabase.getInstance().getReference(Constant.CHAT)
    private lateinit var myUid : String
    private lateinit var targetUser : UserModel
    private lateinit var listOfChat : MutableList<ChatModelRetriever>
    private lateinit var chatListener : ChildEventListener
    private lateinit var messageAdapter : MessageListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        setSupportActionBar(toolbar)
        initComponents()
        retrieveChat()
        sendMessage()
    }

    private fun initComponents(){
        listOfChat = ArrayList()
        val ml = LinearLayoutManager(this@ConversationActivity)
        rv_convo.layoutManager = ml
        messageAdapter = MessageListAdapter(this@ConversationActivity, listOfChat, getMyUid(), getTargetUid())
        rv_convo.adapter = messageAdapter
    }

    private fun getMyUid() : String{
        return intent.getStringExtra("MYUID") ?: throw IllegalArgumentException("MYUID is null") as Throwable
    }

    private fun getTargetUid() : String{
        return intent.getStringExtra("TARGETUID") ?: throw IllegalArgumentException("TYARGET uid is null") as Throwable
    }

    private fun retrieveChat(){
        chatListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(p0.exists()){
                    val message = p0.child("message").value.toString()
                    val tanggal = p0.child("date").value.toString().toLong()
                    val uid = p0.child("uid").value.toString()
                    val messageBody = ChatModelRetriever(uid, tanggal, message)
                    listOfChat.add(messageBody)
                    messageAdapter.notifyDataSetChanged()
                    rv_convo.scrollToPosition(listOfChat.size - 1)


                }


            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        }

        chatRef.child(getMyUid()).child(getTargetUid()).addChildEventListener(chatListener)
    }

    private fun sendMessage(){
        send_chat.setOnClickListener {
            val message = text_chat.text.toString().trim()
            if(!message.isEmpty()){
                val mb = ChatModelUploader(getMyUid(), ServerValue.TIMESTAMP, message)
                chatRef.child(getMyUid()).child(getTargetUid()).push().setValue(mb)
                chatRef.child(getTargetUid()).child(getMyUid()).push().setValue(mb)
                text_chat.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatRef.removeEventListener(chatListener)
    }

}
