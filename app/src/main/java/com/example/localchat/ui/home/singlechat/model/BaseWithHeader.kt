package com.example.localchat.ui.home.singlechat.model

import androidx.room.Ignore
import java.io.Serializable

open class BaseWithHeader(@Ignore var sectionHeaderDate:Long? = null) :Serializable{

}