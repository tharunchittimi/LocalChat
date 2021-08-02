package com.example.localchat.ui.home.singlechat

import com.example.localchat.base.BaseViewModel
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.base.room.relationtables.UsersMessageRelationMain


class SingleChatViewModel : BaseViewModel<SingleChatView>() {
    fun getSingleChatPreviousConversations(
        usersMessageRelationMain: UsersMessageRelationMain?,
        userData: UserEntity?
    ) {
        appDataManager.getDatabaseHelper().getMessagesDao()
            ?.getAllMessagesByGroupId(if (usersMessageRelationMain != null) usersMessageRelationMain.groupId else userData?.groupId)
            .let { previousConversation ->
                navigator?.showSingleChatPreviousConversation(previousConversation)
            }
    }
}