package com.example.localchat.base.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localchat.base.room.dao.RoomDao
import com.example.localchat.base.room.dao.RoomUserDao
import com.example.localchat.base.room.dao.MessagesDao
import com.example.localchat.base.room.dao.UserDao
import com.example.localchat.base.room.entities.RoomEntity
import com.example.localchat.base.room.entities.RoomUsersEntity
import com.example.localchat.base.room.entities.MessagesEntity
import com.example.localchat.base.room.entities.UserEntity

@Database(
    entities = [(UserEntity::class), (MessagesEntity::class), (RoomEntity::class), (RoomUsersEntity::class)],
    version = 5,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getMessageDao(): MessagesDao
    abstract fun getRoomDao(): RoomDao
    abstract fun getRoomUserDao(): RoomUserDao
}