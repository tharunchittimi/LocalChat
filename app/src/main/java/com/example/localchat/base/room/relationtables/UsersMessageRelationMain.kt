package com.example.localchat.base.room.relationtables

import java.io.Serializable

class UsersMessageRelationMain(
    val isGroup: Int,
    val groupId: Long?,
    val groupName: String?,
    val userId: String?,
    val userName: String?,
    val lastMessage: String?,
    val lastMessageId:Long?,
    val lastMessageDate:Long?
) : Serializable
