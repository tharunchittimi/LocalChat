package com.example.localchat.base.room.relationtables

import com.example.localchat.ui.home.singlechat.model.BaseWithHeader
import java.io.Serializable

class RoomMessageRelation(
    var fromUserId: String?,
    var groupId: Long?,
    var message: String?,
    var dateAndTime: Long?,
    val userName:String?,
    val userId:String?
) : Serializable, BaseWithHeader(dateAndTime) {
    var colorCode:Int?=null
}
