package com.example.localchat.base.room

import androidx.room.Room
import com.example.localchat.MyApplication
import com.example.localchat.base.room.dao.RoomDao
import com.example.localchat.base.room.dao.RoomUserDao
import com.example.localchat.base.room.dao.MessagesDao
import com.example.localchat.base.room.dao.UserDao

object DatabaseHelper {

    private var appDataBase: AppDataBase? = null

    private const val DataBaseName = "Internal_DataBase.db"

    fun getUserDataBaseDao(): UserDao? {
        if (appDataBase == null) {
            appDataBase = Room.databaseBuilder(
                MyApplication.getApplicationContext(),
                AppDataBase::class.java, DataBaseName
            ).allowMainThreadQueries().build()
        }
        return appDataBase?.getUserDao()
    }

    fun getMessagesDao(): MessagesDao? {
        if (appDataBase == null) {
            appDataBase = Room.databaseBuilder(
                MyApplication.getApplicationContext(),
                AppDataBase::class.java, DataBaseName
            ).allowMainThreadQueries().build()
        }
        return appDataBase?.getMessageDao()
    }

    fun getRoomDao(): RoomDao? {
        if (appDataBase == null) {
            appDataBase = Room.databaseBuilder(
                MyApplication.getApplicationContext(),
                AppDataBase::class.java, DataBaseName
            ).allowMainThreadQueries().build()
        }
        return appDataBase?.getRoomDao()
    }

    fun getRoomUserDao(): RoomUserDao? {
        if (appDataBase == null) {
            appDataBase = Room.databaseBuilder(
                MyApplication.getApplicationContext(),
                AppDataBase::class.java, DataBaseName
            ).allowMainThreadQueries().build()
        }
        return appDataBase?.getRoomUserDao()
    }

}