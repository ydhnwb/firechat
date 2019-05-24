package com.ydhnwb.firechat.Model

import com.ydhnwb.firechat.Utils.Constant

data class UserModel (val name : String, val email : String, val uid : String, val photoUrl : String){

    constructor() : this("undefined","undefined","undefined", Constant.PHOTO)
}