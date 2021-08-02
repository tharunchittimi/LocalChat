package com.example.localchat.data

import com.ci.chat.data.DataManager
import com.example.localchat.prefrence.PreferenceHelper
import com.ci.chat.prefrence.PreferencesKeys
import com.example.localchat.base.room.DatabaseHelper
import com.example.localchat.base.room.entities.MessagesEntity
import com.example.localchat.base.room.entities.RoomEntity
import com.example.localchat.base.room.entities.RoomUsersEntity
import com.example.localchat.base.room.entities.UserEntity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class AppDataManager private constructor() : DataManager {


    private val preferenceHelper: PreferenceHelper = PreferenceHelper.getInstance()

    private val gson = GsonBuilder().create()

    fun getDatabaseHelper(): DatabaseHelper {
        return DatabaseHelper
    }

    companion object {
        private var instance: AppDataManager? = null
        fun getMyInstance(): AppDataManager {
            if (instance == null) {
                instance =
                    AppDataManager()
            }
            return instance!!
        }
    }

    override fun setFCMToken(token: String) {
        preferenceHelper.putString(PreferencesKeys.fcm_token, token)
    }

    override fun getFCMToken(): String {
        return preferenceHelper.getString(PreferencesKeys.fcm_token)
    }

    override fun setApplicationOnStatus(status: Boolean) {
        preferenceHelper.putBoolean(PreferencesKeys.application_status, status)
    }

    override fun getApplicationOnStatus(): Boolean {
        return preferenceHelper.getBoolean(PreferencesKeys.application_status)
    }

    override fun setCurrentUserEntity(userEntity: UserEntity?) {
        preferenceHelper.putString(PreferencesKeys.current_user_entity, gson.toJson(userEntity))
    }

    override fun getCurrentUserEntity(): UserEntity? {
        val jsonString =
            preferenceHelper.getString(PreferencesKeys.current_user_entity)
        val type = object : TypeToken<UserEntity?>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun sendMessage(toUserId: String?, groupId: Long?, message: String?) {
        val currentUserId = getCurrentUserEntity()?.userId
        if (groupId != null) {
            insertMessage(currentUserId, toUserId, groupId, message)
        } else {

            val groupEntity = RoomEntity(null, null, 0, System.currentTimeMillis())
            getDatabaseHelper().getRoomDao()?.insertGroup(groupEntity).let {
                insertGroupMembers(it ?: 0, arrayOf(toUserId, currentUserId))
                getDatabaseHelper().getRoomDao()?.getInsertedGroupData(it).let { groupEntity ->
                    insertMessage(currentUserId, toUserId, groupEntity?.groupId, message)
                }
            }
        }
    }

    fun createGroup(
        groupName: String?,
        groDesc: String?,
        userIds: ArrayList<UserEntity?>?
    ): Long? {
        val groupEntity = RoomEntity(groupName, groDesc, 1, System.currentTimeMillis())
        getDatabaseHelper().getRoomDao()?.insertGroup(groupEntity).let {
            insertGroupMembers(it ?: 0, userIds ?: ArrayList())
            return it
        }
    }

    private fun insertGroupMembers(groupId: Long, userIds: Array<String?>) {
        val list = ArrayList<RoomUsersEntity>()
        for (id in userIds) {
            val groupUsersEntity = RoomUsersEntity(groupId, id ?: "")
            list.add(groupUsersEntity)
        }
        getDatabaseHelper().getRoomUserDao()?.insertGroupUsers(list)
    }

    private fun insertGroupMembers(groupId: Long, userIds: ArrayList<UserEntity?>) {
        val list = ArrayList<RoomUsersEntity>()
        for (userEntity in userIds) {
            val groupUsersEntity = RoomUsersEntity(groupId, userEntity?.userId ?: "")
            list.add(groupUsersEntity)
        }
        getDatabaseHelper().getRoomUserDao()?.insertGroupUsers(list)
    }

    private fun insertMessage(
        currentUserId: String?,
        toUserId: String?,
        groupId: Long?,
        message: String?
    ) {
        val messageEntity = MessagesEntity(
            currentUserId,
            toUserId,
            groupId,
            message,
            System.currentTimeMillis()
        )
        getDatabaseHelper().getMessagesDao()?.insertMessage(messageEntity)
    }

    fun sendGroupMessage(groupId: Long?, message: String?) {
        val currentUserId = getCurrentUserEntity()?.userId
        insertMessage(currentUserId, null, groupId, message)
    }
}