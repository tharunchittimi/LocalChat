package com.example.localchat.base.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["mGroupId","userIds"])
class RoomUsersEntity(
    @field:ColumnInfo(name = "mGroupId")
    var mGroupId: Long,
    @field:ColumnInfo(name = "userIds")
    var userIds: String
)