package com.example.localchat.ui.home.groupchat

import com.example.localchat.base.BaseViewModel


class GroupChatViewModel : BaseViewModel<GroupChatView>() {
    fun getPreviousConversations(grpId: Long?) {
        appDataManager.getDatabaseHelper().getMessagesDao()
            ?.getGroupMessagesByGroupId(grpId)?.let { previousConversations->
                navigator?.showPreviousConversations(previousConversations)
            }
    }
}