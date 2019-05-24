package com.ydhnwb.firechat.FirebaseHelper

import com.google.firebase.database.FirebaseDatabase
import com.ydhnwb.firechat.Model.UserModel
import com.ydhnwb.firechat.Utils.Constant
import java.text.DateFormat
import java.util.*

class FirebaseHelper {
    companion object {
        val mFirebaseDatabase = FirebaseDatabase.getInstance()

        fun pushUserData(name : String, email : String, uid : String){
            val mref = mFirebaseDatabase.getReference(Constant.USERS)
            val me = UserModel(name,email,uid,Constant.PHOTO)
            mref.child(uid).setValue(me)
        }

        fun getTimeDate(timeStamp : Long) : String {
            return try{
                val dateFormat = DateFormat.getDateTimeInstance()
                val netDate = Date(timeStamp)
                dateFormat.format(netDate)
            }catch (e : Exception){
                "undefined date"
            }
        }
    }
}