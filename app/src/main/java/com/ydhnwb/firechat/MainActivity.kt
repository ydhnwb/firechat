package com.ydhnwb.firechat

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ydhnwb.firechat.Interfaces.MyClickListener
import com.ydhnwb.firechat.Model.UserModel
import com.ydhnwb.firechat.Utils.Constant
import com.ydhnwb.firechat.ViewHolder.SimpleListViewHolder

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef : DatabaseReference
    private lateinit var mUserModel : UserModel
    private lateinit var myUid : String
    private lateinit var fireAdapter : FirebaseRecyclerAdapter<UserModel, SimpleListViewHolder>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initComponents()
        initAuth()
    }



    private fun initAuth(){
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val me = it.currentUser
            if(me == null){
                val i = Intent(this@MainActivity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }else{
                myUid = me.uid
                initFire()
                fetchData()
                fab.setOnClickListener { view ->
                    val i = Intent(this@MainActivity,NewChatActivity::class.java)
                    i.putExtra("MYUID", myUid)
                    startActivity(i)
                }
            }
        }

        mAuth.addAuthStateListener(mAuthListener)
    }


    private fun initFire(){
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.getReference(Constant.USERS)
        getMyData()
    }

    private fun getMyData(){
        mRef.child(myUid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    mUserModel = p0.getValue(UserModel::class.java)!!
                    //you can use this to fetch dtata like profile pic or name
                }
            }

        })
    }


    private fun fetchData(){
        val cref = mFirebaseDatabase.getReference(Constant.CHAT)
        val fo = FirebaseRecyclerOptions.Builder<UserModel>().setQuery(
                cref.child(myUid), UserModel::class.java
        ).build()

        fireAdapter = object : FirebaseRecyclerAdapter<UserModel, SimpleListViewHolder>(fo){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleListViewHolder {

                return SimpleListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_daftar_chat,
                        parent, false), this@MainActivity)
            }

            override fun onBindViewHolder(holder: SimpleListViewHolder, position: Int, model: UserModel) {

                mRef.child(getRef(position).key.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            val user = p0.getValue(UserModel::class.java)
                            if(user != null){
                                holder.displayName.text = user.name
                                Glide.with(this@MainActivity).load(user.photoUrl).into(holder.photoProfile)
                            }
                        }
                    }
                })

                holder.setOnItemClickListener(object : MyClickListener{
                    override fun onClick(v: View, position: Int, isLongClick: Boolean) {
                        val i = Intent(this@MainActivity, ConversationActivity::class.java)
                        i.putExtra("MYUID", myUid)
                        i.putExtra("TARGETUID", getRef(position).key.toString())
                        startActivity(i)
                    }
                })

                holder.setOnLongItemClick(object : MyClickListener{
                    override fun onClick(v: View, position: Int, isLongClick: Boolean) {
                        Toast.makeText(this@MainActivity, "Replace with your own action", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

        fireAdapter.startListening()
        rv_list_chat.adapter = fireAdapter
    }


    private fun initComponents(){
        val mLayoutmanager = LinearLayoutManager(this@MainActivity)
        mLayoutmanager.stackFromEnd = true
        mLayoutmanager.reverseLayout = true
        rv_list_chat.layoutManager = mLayoutmanager
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                mAuth.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
