package com.ydhnwb.firechat.Model

data class ChatModelRetriever (val uid : String, val date : Long, val message : String){
    //we need some default constructor
    constructor() : this("",0,"")
}