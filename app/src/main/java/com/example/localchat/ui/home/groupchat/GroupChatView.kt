package com.example.localchat.ui.home.groupchat

import com.example.localchat.base.BaseNavigator
import com.example.localchat.base.room.relationtables.RoomMessageRelation


interface GroupChatView : BaseNavigator {
    fun showPreviousConversations(previousConversations: List<RoomMessageRelation?>)
}