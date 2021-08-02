package com.example.localchat.ui.home.groupchat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.localchat.R
import com.example.localchat.BR
import com.example.localchat.ui.home.chat.ChatHomeActivity
import com.example.localchat.ui.home.groupchat.adapter.GroupChatAdapter
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.base.room.relationtables.RoomMessageRelation
import com.example.localchat.databinding.ActivityGroupChatBinding
import com.example.localchat.util.*
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity :
    BaseActivity<ActivityGroupChatBinding, GroupChatView, GroupChatViewModel>(), GroupChatView {

    private var usersData: ArrayList<UserEntity>? = null
    private var grpName: String? = null
    private var grpId: Long? = null
    private var groupChatAdapter: GroupChatAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun getContentView(): Int {
        return R.layout.activity_group_chat
    }

    override fun setViewModelClass(): Class<GroupChatViewModel> {
        return GroupChatViewModel::class.java
    }

    override fun getNavigator(): GroupChatView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.groupChatViewModel
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        getIntentData()
        setUpRecyclerview()
        setUpUi()
        setUpViewClickListeners()
        showConversations()
    }

    private fun showConversations() {
        mViewModel?.getPreviousConversations(grpId)
    }

    override fun showPreviousConversations(previousConversations: List<RoomMessageRelation?>) {
        if (previousConversations.size ?: 0 <= 0) {
            mViewDataBinding?.llvNoData?.visibility = View.VISIBLE
        } else {
            groupChatAdapter?.addItems(previousConversations as ArrayList<RoomMessageRelation>)
            mViewDataBinding?.rvChatList?.visibility = View.VISIBLE
            mViewDataBinding?.bottomlayout?.visibility = View.VISIBLE
            mViewDataBinding?.llvNoData?.visibility = View.GONE
        }
    }

    private fun setUpRecyclerview() {
        groupChatAdapter = GroupChatAdapter(getCurrentLoggedInUserData())
        groupChatAdapter?.setListener(object : GroupChatAdapter.Listener {
            override fun mustScrollTOLast() {
                if (groupChatAdapter?.itemCount ?: 0 > 0) {
                    mViewDataBinding?.rvChatList?.post {
                        // mLayoutManager?.scrollToPosition((chattingAdapter?.itemCount ?: 1) - 1)
                        mViewDataBinding?.rvChatList?.scrollToPosition(
                            (groupChatAdapter?.itemCount ?: 1) - 1
                        )
                    }
                }
            }

            override fun scrollToLast() {
                if (groupChatAdapter?.itemCount ?: 0 > 0) {
                    mViewDataBinding?.rvChatList?.post {
                        val min = ((groupChatAdapter?.itemCount ?: 1) - 40)
                        val max = ((groupChatAdapter?.itemCount ?: 1) - 1)
                        val currentLastPosition = mLayoutManager?.findLastVisibleItemPosition() ?: 0
                        if ((min < currentLastPosition) && (currentLastPosition < max)) {
                            // mLayoutManager?.scrollToPosition((chattingAdapter?.itemCount ?: 1) - 1)
                            mViewDataBinding?.rvChatList?.scrollToPosition(
                                (groupChatAdapter?.itemCount ?: 1) - 1
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
            it.adapter = groupChatAdapter
            it.addItemDecoration(StickHeaderItemDecoration(groupChatAdapter!!))
        }
    }

    private fun setUpUi() {
        mViewDataBinding?.toolBarGroupChat?.txtToolTitle?.text = grpName
        val userNames = mViewModel?.appDataManager?.getDatabaseHelper()?.getRoomUserDao()
            ?.getGroupMembers(grpId, getCurrentLoggedInUserData()?.userId)
        val contactNames = StringBuilder("You")
        for (i in userNames ?: ArrayList()) {
            contactNames.append(", $i")
        }
        mViewDataBinding?.toolBarGroupChat?.txtToolContactsCount?.text = "$contactNames"
        mViewDataBinding?.toolBarGroupChat?.imgProfile?.visibility = View.VISIBLE
        mViewDataBinding?.toolBarGroupChat?.imgProfile?.let { iv ->
            Glide.with(iv.context).load("")
                .apply(
                    RequestOptions().circleCrop().error(R.drawable.ic_default_group_profile)
                )
                .into(iv)
        }
    }

    private fun getIntentData() {
        if (intent?.hasExtra(BUNDLE_USER_DATA) == true) {
            usersData =
                intent?.getSerializableExtra(BUNDLE_USER_DATA) as ArrayList<UserEntity>
        }
        if (intent?.hasExtra(BUNDLE_GROUP_NAME) == true) {
            grpName = intent?.getStringExtra(BUNDLE_GROUP_NAME)
        }
        if (intent?.hasExtra(BUNDLE_GROUP_ID) == true) {
            grpId = intent?.getLongExtra(BUNDLE_GROUP_ID, 0L)
        }
    }

    private fun setUpToolBar() {
        mViewDataBinding?.toolBarGroupChat?.let {
            it.imgToolLeft.visibility = View.VISIBLE
            it.txtToolContactsCount.visibility = View.VISIBLE
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
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
        val groupMessageRelation = RoomMessageRelation(
            getCurrentLoggedInUserData()?.userId,
            grpId,
            message,
            calendar.timeInMillis,
            getCurrentLoggedInUserData()?.userName,
            getCurrentLoggedInUserData()?.userId
        )
        mViewModel?.appDataManager?.sendGroupMessage(grpId, message)
        groupChatAdapter?.addSendMessage(
            groupMessageRelation
        )
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ChatHomeActivity::class.java))
        finishAffinity()
    }
}