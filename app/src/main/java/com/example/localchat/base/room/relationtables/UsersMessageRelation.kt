package com.example.localchat.base.room.relationtables

import androidx.room.Embedded
import com.example.localchat.base.room.entities.MessagesEntity
import com.example.localchat.base.room.entities.UserEntity

class UsersMessageRelation(
    @Embedded
    val messagesEntity: MessagesEntity,
    @Embedded
    val userEntity: UserEntity

)