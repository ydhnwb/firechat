package com.ydhnwb.firechat.Interfaces

import android.view.View

interface MyClickListener {
    abstract fun onClick(v : View, position : Int, isLongClick : Boolean)
}