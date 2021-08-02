package com.example.localchat.base.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import java.io.Serializable

@Entity(primaryKeys = ["userId"],tableName = "userEntity")
class UserEntity(
    @field:ColumnInfo(name = "userId")
    var userId: String,
    @field:ColumnInfo(name = "userName")
    var userName: String,
    @field:ColumnInfo(name = "phoneNumber")
    var phoneNumber: String,
    @field:ColumnInfo(name = "password")
    var password: String

):Serializable {
    @field:ColumnInfo(name = "lastOpenedTime")
    var lastOpenedTime: String?= null
    @Ignore
    var isSelected = false
    @Ignore
    var groupId:Long? = null
}