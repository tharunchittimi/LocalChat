package com.example.localchat.base.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localchat.base.room.entities.RoomEntity

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(roomEntity: RoomEntity): Long

//    @Query("SELECT LAST_INSERT_ID() from GroupEntity")
//    fun getLastGroupId():Long

    @Query("SELECT * from RoomEntity where groupId = :groupId limit 1")
    fun getInsertedGroupData(groupId:Long?): RoomEntity


    @Query("select RE.groupId from RoomUsersEntity as RUE join RoomEntity as RE on RUE.mGroupId = RE.groupId join  (select RE1.groupId from RoomUsersEntity as RUE1  join RoomEntity as RE1 on RUE1.mGroupId = RE1.groupId where RE1.isGroup = 0 and RUE1.userIds = :currentUserId) as Q1 on Q1.groupId = RE.groupId where RUE.userIds = :toUserId  and RE.isGroup = 0")
    fun getGroupId(currentUserId:String?,toUserId:String?):Long?



}