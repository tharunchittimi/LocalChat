package com.example.localchat.ui.home.singlechat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.localchat.R
import com.example.localchat.BR
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.entities.MessagesEntity
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.base.room.relationtables.UsersMessageRelationMain
import com.example.localchat.databinding.ActivitySingleChatBinding
import com.example.localchat.ui.home.chat.ChatHomeActivity
import com.example.localchat.ui.home.singlechat.adapter.SingleChatAdapter
import com.example.localchat.util.BUNDLE_USER_DATA
import com.example.localchat.util.BUNDLE_Users_Message_Relation_Main
import com.example.localchat.util.StickHeaderItemDecoration
import com.example.localchat.util.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SingleChatActivity :
    BaseActivity<ActivitySingleChatBinding, SingleChatView, SingleChatViewModel>(), SingleChatView {

    private var usersMessageRelationMain: UsersMessageRelationMain? = null
    private var userData: UserEntity? = null
    private var singleChatAdapter: SingleChatAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    companion object {
        var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        var simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    override fun getContentView(): Int {
        return R.layout.activity_single_chat
    }

    override fun setViewModelClass(): Class<SingleChatViewModel> {
        return SingleChatViewModel::class.java
    }

    override fun getNavigator(): SingleChatView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.singleChatViewModel
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        getIntentData()
        setUpUi()
        setUpRecyclerview()
        setUpViewClickListeners()
        showConversations()
    }

    private fun showConversations() {
        mViewModel?.getSingleChatPreviousConversations(usersMessageRelationMain, userData)
    }

    override fun showSingleChatPreviousConversation(previousConversation: List<MessagesEntity?>?) {
        if (previousConversation?.size ?: 0 <= 0) {
            mViewDataBinding?.llvNoData?.visibility = View.VISIBLE
        } else {
            singleChatAdapter?.addItems(previousConversation as ArrayList<MessagesEntity>)
            mViewDataBinding?.rvChatList?.visibility = View.VISIBLE
            mViewDataBinding?.bottomlayout?.visibility = View.VISIBLE
            mViewDataBinding?.llvNoData?.visibility = View.GONE
        }
    }

    private fun setUpViewClickListeners() {
        mViewDataBinding?.btnStartChat?.setOnClickListener {
            mViewDataBinding?.llvNoData?.visibility = View.GONE
            mViewDataBinding?.rvChatList?.visibility = View.VISIBLE
            mViewDataBinding?.bottomlayout?.visibility = View.VISIBLE
        }
        mViewDataBinding?.enterChat?.setOnClickListener {
            val message = mViewDataBinding?.chatEditText1?.text?.toString()?.trim()
            if (message?.length ?: 0 > 0) {
                sendMessage(message ?: "")
                mViewDataBinding?.chatEditText1?.setText("")
            } else {
                showMessage("Enter a message")
            }
        }
    }

    private fun sendMessage(message: String) {
        val calendar = Calendar.getInstance()
        val messagesEntity = MessagesEntity(
            getCurrentLoggedInUserData()?.userId,
            usersMessageRelationMain?.userId,
            usersMessageRelationMain?.groupId,
            message,
            calendar.timeInMillis
        )
        singleChatAdapter?.addSendMessage(
            messagesEntity
        )
        if (usersMessageRelationMain != null) {
            mViewModel?.appDataManager?.sendMessage(
                usersMessageRelationMain?.userId,
                usersMessageRelationMain?.groupId,
                message
            )
        } else {
            if (userData?.groupId == null) {
                mViewModel?.appDataManager?.getDatabaseHelper()?.getRoomDao()
                    ?.getGroupId(getCurrentLoggedInUserData()?.userId, userData?.userId)
                    .let { groupId ->
                        userData?.groupId = groupId
                        mViewModel?.appDataManager?.sendMessage(userData?.userId, groupId, message)
                    }
            } else {
                mViewModel?.appDataManager?.sendMessage(
                    userData?.userId,
                    userData?.groupId,
                    message
                )
            }
        }
    }


    private fun setUpRecyclerview() {
        singleChatAdapter = SingleChatAdapter(getCurrentLoggedInUserData())
        singleChatAdapter?.setListener(object : SingleChatAdapter.Listener {
            override fun mustScrollTOLast() {
                if (singleChatAdapter?.itemCount ?: 0 > 0) {
                    mViewDataBinding?.rvChatList?.post {
                        // mLayoutManager?.scrollToPosition((chattingAdapter?.itemCount ?: 1) - 1)
                        mViewDataBinding?.rvChatList?.scrollToPosition(
                            (singleChatAdapter?.itemCount ?: 1) - 1
                        )
                    }
                }
            }

            override fun scrollToLast() {
                if (singleChatAdapter?.itemCount ?: 0 > 0) {
                    mViewDataBinding?.rvChatList?.post {
                        val min = ((singleChatAdapter?.itemCount ?: 1) - 40)
                        val max = ((singleChatAdapter?.itemCount ?: 1) - 1)
                        val currentLastPosition = mLayoutManager?.findLastVisibleItemPosition() ?: 0
                        if ((min < currentLastPosition) && (currentLastPosition < max)) {
                            // mLayoutManager?.scrollToPosition((chattingAdapter?.itemCount ?: 1) - 1)
                            mViewDataBinding?.rvChatList?.scrollToPosition(
                                (singleChatAdapter?.itemCount ?: 1) - 1
                            )
                        }
                    }
                }
            }

        })
        mLayoutManager = LinearLayoutManager(this)
        mViewDataBinding?.rvChatList?.let {
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.layoutManager = mLayoutManager
            it.adapter = singleChatAdapter
            it.addItemDecoration(StickHeaderItemDecoration(singleChatAdapter!!))

        }
    }

    private fun setUpUi() {
        mViewDataBinding?.toolBarSingleChat?.txtToolTitle?.text =
            if (usersMessageRelationMain != null) usersMessageRelationMain?.userName else userData?.userName
        mViewDataBinding?.toolBarSingleChat?.imgProfile?.visibility = View.VISIBLE
        mViewDataBinding?.toolBarSingleChat?.imgProfile?.let { iv ->
            Glide.with(iv.context).load("")
                .apply(
                    RequestOptions().circleCrop().error(R.drawable.ic_default_profile)
                )
                .into(iv)
        }
    }

    private fun getIntentData() {
        if (intent?.hasExtra(BUNDLE_Users_Message_Relation_Main) == true) {
            usersMessageRelationMain =
                intent?.getSerializableExtra(BUNDLE_Users_Message_Relation_Main) as UsersMessageRelationMain
        } else if (intent?.hasExtra(BUNDLE_USER_DATA) == true) {
            userData =
                intent?.getSerializableExtra(BUNDLE_USER_DATA) as UserEntity

            mViewModel?.appDataManager?.getDatabaseHelper()?.getRoomDao()
                ?.getGroupId(getCurrentLoggedInUserData()?.userId, userData?.userId)
                .let { groupId ->
                    if (groupId != null) {
                        userData?.groupId = groupId
                    }
                }
        }
    }

    private fun setUpToolBar() {
        mViewDataBinding?.toolBarSingleChat?.let {
            it.imgToolLeft.visibility = View.VISIBLE
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ChatHomeActivity::class.java))
        finishAffinity()
    }
}