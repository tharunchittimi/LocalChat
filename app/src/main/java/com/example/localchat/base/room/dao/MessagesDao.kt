package com.example.localchat.base.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localchat.base.room.entities.MessagesEntity
import com.example.localchat.base.room.relationtables.RoomMessageRelation
import com.example.localchat.base.room.relationtables.UsersMessageRelationMain

@Dao
interface MessagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messagesEntity: MessagesEntity): Long

    @Query("select RE.isGroup,RE.groupId,RE.groupName,Q1.userId,Q1.userName,Q2.lastMessage,Q2.lastMessageId,Q2.lastMessageDate from RoomUsersEntity as RUE left join ROOMENTITY RE on RE.groupId = RUE.mGroupId left join (select * from RoomUsersEntity RUE1 join userEntity UE1 on RUE1.userIds = UE1.userId where UE1.userId !=:currentUserId  group by RUE1.mGroupId ) as Q1 on Q1.mGroupId = RE.groupId left join (select MAX(ME.messageId) as max, ME.messageId as lastMessageId,ME.message as lastMessage,RUE.mGroupId,ME.dateAndTime as lastMessageDate from messageEntity as ME join RoomUsersEntity as RUE on RUE.mGroupId = ME.myGroupId  join ROOMENTITY as RE on RE.groupId = RUE.mGroupId where RUE.userIds = :currentUserId GROUP BY ME.myGroupId order by ME.dateAndTime DESC) as Q2 on Q2.mGroupId = RE.groupId  where RUE.userIds = :currentUserId order by COALESCE(Q2.lastMessageDate,RE.createdDate) DESC")
    fun getAllGroupsByUserIdMain(currentUserId: String?): List<UsersMessageRelationMain?>?


    @Query("SELECT * FROM messageEntity where myGroupId =:groupId ORDER BY dateAndTime ASC")
    fun getAllMessagesByGroupId(groupId: Long?): List<MessagesEntity?>?


    @Query("select ME.fromUserId,ME.myGroupId,ME.message,ME.dateAndTime, UE.userName,UE.userId From messageEntity ME join userEntity UE on UE.userId = ME.fromUserId where ME.myGroupId =:groupId order by ME.dateAndTime ASC")
    fun getGroupMessagesByGroupId(groupId: Long?): List<RoomMessageRelation?>?

}