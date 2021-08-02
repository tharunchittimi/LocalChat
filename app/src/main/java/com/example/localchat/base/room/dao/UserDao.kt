package com.example.localchat.base.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localchat.base.room.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserEntity): Long

    @Query("SELECT * FROM userEntity WHERE userId != :currentUserId")
    fun getAllUsers(currentUserId:String?): List<UserEntity?>?

    @Query("Update userEntity SET lastOpenedTime = :lastOpenedTime WHERE userId = :userId")
    fun updateLast(userId:String?,lastOpenedTime:String?)

    @Query("Select * from userEntity WHERE phoneNumber = :phoneNumber and password =:password")
    fun checkUserByPhoneNumber(phoneNumber:String?,password:String?): UserEntity?

}