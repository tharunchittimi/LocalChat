package com.example.localchat.base.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomEntity(
    @field:ColumnInfo(name = "groupName")
    var groupName: String?,
    @field:ColumnInfo(name = "groupDesc")
    var groupDesc: String?,
    @field:ColumnInfo(name = "isGroup")
    var isGroup: Int,
    @field:ColumnInfo(name = "createdDate")
    var createdDate: Long
) {
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "groupId")
    var groupId: Long = 0
}