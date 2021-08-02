package com.example.localchat.base.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.localchat.ui.home.singlechat.model.BaseWithHeader
import java.io.Serializable

@Entity(tableName = "messageEntity")
class MessagesEntity(
    @field:ColumnInfo(name = "fromUserId")
    var fromUserId: String?,
    @field:ColumnInfo(name = "toUserId")
    var toUserId: String?,
    @field:ColumnInfo(name = "myGroupId")
    var myGroupId: Long?,
    @field:ColumnInfo(name = "message")
    var message: String?,
    @field:ColumnInfo(name = "dateAndTime")
    var dateAndTime: Long?,


): BaseWithHeader(dateAndTime),Serializable {
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "messageId")
    var messageId: Int = 0

}