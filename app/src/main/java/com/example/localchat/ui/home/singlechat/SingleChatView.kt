package com.example.localchat.ui.home.singlechat

import com.example.localchat.base.BaseNavigator
import com.example.localchat.base.room.entities.MessagesEntity


interface SingleChatView : BaseNavigator {
    fun showSingleChatPreviousConversation(previousConversation: List<MessagesEntity?>?)
}