package com.example.localchat.prefrence

import com.example.localchat.base.room.entities.UserEntity

interface Prefs {
    fun setFCMToken(token: String)
    fun getFCMToken(): String
    fun setApplicationOnStatus(status: Boolean)
    fun getApplicationOnStatus(): Boolean
    fun setCurrentUserEntity(userEntity: UserEntity?)
    fun getCurrentUserEntity(): UserEntity?
}