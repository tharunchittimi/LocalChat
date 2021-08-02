package com.example.localchat.ui.home.selectcontact

import com.example.localchat.base.BaseNavigator
import com.example.localchat.base.room.entities.UserEntity


interface SelectContactView : BaseNavigator {
    fun updateData(selectContactList: List<UserEntity?>?)
}