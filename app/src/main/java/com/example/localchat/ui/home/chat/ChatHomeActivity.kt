package com.example.localchat.ui.home.chat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.localchat.BR
import com.example.localchat.R
import com.example.localchat.ui.home.chat.adapter.ChatHomeAdapter
import com.example.localchat.ui.home.groupchat.GroupChatActivity
import com.example.localchat.ui.home.selectcontact.SelectContactActivity
import com.example.localchat.ui.home.singlechat.SingleChatActivity
import com.example.localchat.ui.login.LoginActivity
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.relationtables.UsersMessageRelationMain
import com.example.localchat.databinding.ActivityNavBinding
import com.example.localchat.util.BUNDLE_GROUP_ID
import com.example.localchat.util.BUNDLE_GROUP_NAME
import com.example.localchat.util.BUNDLE_Users_Message_Relation_Main
import com.example.localchat.widgets.CustomButton
import com.example.localchat.widgets.CustomTextView
import kotlinx.android.synthetic.main.activity_chat_home.view.*
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.content_menu.view.*
import kotlinx.android.synthetic.main.inflate_toolbar.view.*
import java.util.*

class ChatHomeActivity : BaseActivity<ActivityNavBinding, ChatHomeView, ChatHomeViewModel>(),
    ChatHomeView {

    private var chatHomeAdapter: ChatHomeAdapter? = null
    private var clickedTime = 0L
    private var alertDialog: AlertDialog? = null

    override fun getContentView(): Int {
        return R.layout.activity_nav
    }

    override fun setViewModelClass(): Class<ChatHomeViewModel> {
        return ChatHomeViewModel::class.java
    }

    override fun getNavigator(): ChatHomeView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.chatHomeViewModel
    }

    override fun onResume() {
        mViewModel?.appDataManager?.getDatabaseHelper()?.getMessagesDao()
            ?.getAllGroupsByUserIdMain(getCurrentLoggedInUserData()?.userId).let { list ->
                if (list?.size ?: 0 <= 0) {
                    mViewDataBinding?.contentMain?.llvNoData?.visibility = View.VISIBLE
                } else {
                    chatHomeAdapter?.addList(list ?: ArrayList())
                    mViewDataBinding?.contentMain?.grpRvItem?.visibility = View.VISIBLE
                    mViewDataBinding?.contentMain?.llvNoData?.visibility = View.GONE
                }
            }
        super.onResume()
    }

    private fun setUpNavigationDrawer() {
        contentMain.toolBarChatHome?.imgToolRight?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        setUpRecyclerView()
        setUpClickListener()
        setUpNavigationDrawer()
    }

    private fun setUpClickListener() {
        mViewDataBinding?.contentMain?.btnChatList?.setOnClickListener {
            startActivity(Intent(this, SelectContactActivity::class.java))
        }
        navView?.tvSignOut?.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            Handler(Looper.getMainLooper()).postDelayed({
                showSignOutPopUp()
            }, 200)
        }
        navView?.tvChatTitle?.text = "${getCurrentLoggedInUserData()?.userName}"
        navView?.tvPhoneNumber?.text = getCurrentLoggedInUserData()?.phoneNumber

    }

    private fun setUpRecyclerView() {
        chatHomeAdapter = ChatHomeAdapter()
        mViewDataBinding?.contentMain?.rvChatHomeList?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = chatHomeAdapter
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        chatHomeAdapter?.setOnItemClickListener(object : ChatHomeAdapter.OnItemClick {
            override fun onChatItemClick(chatHomeModel: UsersMessageRelationMain?) {
                if (chatHomeModel?.isGroup ?: 0 == 0) {
                    val createGroupIntent =
                        Intent(this@ChatHomeActivity, SingleChatActivity::class.java)
                    createGroupIntent.putExtra(BUNDLE_Users_Message_Relation_Main, chatHomeModel)
                    startActivity(createGroupIntent)
                } else {
                    val groupIntent = Intent(this@ChatHomeActivity, GroupChatActivity::class.java)
                    groupIntent.putExtra(BUNDLE_GROUP_NAME, chatHomeModel?.groupName)
                    groupIntent.putExtra(BUNDLE_GROUP_ID, chatHomeModel?.groupId)
                    startActivity(groupIntent)
                }
            }

        })
    }

    private fun setUpToolBar() {
        mViewDataBinding?.contentMain?.toolBarChatHome?.let {
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.imgToolRight.setImageResource(R.drawable.ic_menu)
            it.imgToolRight.visibility = View.VISIBLE
            it.txtToolTitle.text = "CHATS"
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private var btnSendSignOut: CustomButton? = null

    private fun showSignOutPopUp() {
        if (alertDialog?.isShowing == true) {
            alertDialog?.hide()
        }
        val builder = AlertDialog.Builder(this, R.style.MyDialogPopup)
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.inflate_signout_alert, null, false)
        val txtSignOutCancel: CustomTextView = view.findViewById(R.id.txtSignOutCancel)
        btnSendSignOut = view.findViewById(R.id.btnSendSignOut)
        btnSendSignOut?.setOnClickListener {
            mViewModel?.appDataManager?.setCurrentUserEntity(null)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        txtSignOutCancel.setOnClickListener {
            alertDialog?.dismiss()
        }
        builder.setView(view)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog?.show()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        when {
            drawerLayout.isDrawerOpen(GravityCompat.END) -> {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
            (currentTime - clickedTime) <= 1500 -> {
                super.onBackPressed()
            }
            else -> {
                clickedTime = currentTime
                showMessage("Press Again to Exit")
            }
        }
    }
}