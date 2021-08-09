package com.example.localchat.base.room.dao

import androidx.room.*
import com.example.localchat.base.room.entities.RoomUsersEntity

@Dao
interface RoomUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroupUsers(roomUsersEntity: List<RoomUsersEntity>)

    @Query("select UE.userName from ROOMUSERSENTITY RUE left join userEntity UE on RUE.userIds = UE.userId where RUE.mGroupId = :groupId and RUE.userIds != :currentUserId ")
    fun getGroupMembers(groupId:Long?, currentUserId:String?): List<String?>
}