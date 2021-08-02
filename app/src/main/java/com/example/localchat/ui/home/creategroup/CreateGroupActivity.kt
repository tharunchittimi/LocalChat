package com.example.localchat.ui.home.creategroup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.localchat.R
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.ActivityCreateGroupBinding
import com.example.localchat.ui.home.creategroup.adapter.CreateGroupAdapter
import com.example.localchat.BR
import com.example.localchat.ui.home.creategroup.adapter.GridSpacingItemDecoration
import com.example.localchat.ui.home.groupchat.GroupChatActivity
import com.example.localchat.util.BUNDLE_GROUP_ID
import com.example.localchat.util.BUNDLE_GROUP_NAME
import com.example.localchat.util.BUNDLE_USERS_SELECTED
import com.example.localchat.util.BUNDLE_USER_DATA


class CreateGroupActivity :
    BaseActivity<ActivityCreateGroupBinding, CreateGroupView, CreateGroupViewModel>(),
    CreateGroupView {

    private var createGroupAdapter: CreateGroupAdapter? = null
    private var usersCount: Int? = null
    private var usersData: ArrayList<UserEntity?>? = null

    override fun getContentView(): Int {
        return R.layout.activity_create_group
    }

    override fun setViewModelClass(): Class<CreateGroupViewModel> {
        return CreateGroupViewModel::class.java
    }

    override fun getNavigator(): CreateGroupView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.createGroupViewModel
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        getIntentData()
        setUpRecyclerView()
        setViewClickListener()
    }

    private fun setViewClickListener() {
        mViewDataBinding?.btnCreateGrp?.setOnClickListener {
            val grpName = mViewDataBinding?.edtTxtGroupName?.text?.toString()
            if (grpName?.length ?: 0 > 0) {
                usersData?.add(getCurrentLoggedInUserData())
                mViewModel?.appDataManager?.createGroup(grpName, grpName, usersData).let {
                    val groupIntent = Intent(this, GroupChatActivity::class.java)
                    groupIntent.putExtra(BUNDLE_GROUP_NAME, grpName)
                    groupIntent.putExtra(BUNDLE_GROUP_ID, it)
                    startActivity(groupIntent)
                }

            } else {
                showMessage("Provide a group subject")
            }
        }
    }

    private fun getIntentData() {
        if (intent?.hasExtra(BUNDLE_USERS_SELECTED) == true) {
            usersCount = intent?.getIntExtra(BUNDLE_USERS_SELECTED, -1)
        }
        if (intent?.hasExtra(BUNDLE_USER_DATA) == true) {
            usersData =
                intent?.getSerializableExtra(BUNDLE_USER_DATA) as ArrayList<UserEntity?>
        }
    }

    private fun setUpRecyclerView() {
        createGroupAdapter = CreateGroupAdapter()
        mViewDataBinding?.rvCreateGrpList?.apply {
            layoutManager = GridLayoutManager(this@CreateGroupActivity, 4)
            val spanCount = 4
            val spacing = 50
            val includeEdge = true
            mViewDataBinding?.rvCreateGrpList?.addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount,
                    spacing,
                    includeEdge
                )
            )
            adapter = createGroupAdapter
            itemAnimator?.changeDuration = 0
        }
        addData()
    }

    private fun addData() {
        mViewDataBinding?.tvTotalParticipants?.text = "Participants: $usersCount"
        usersData?.let { createGroupAdapter?.addList(it) }
    }

    private fun setUpToolBar() {
        mViewDataBinding?.toolBarCreateGroup?.let {
            it.imgToolLeft.visibility = View.VISIBLE
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.txtToolTitle.text = "NEW GROUP"
            it.txtToolContactsCount.text = "Add subject"
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
        }
    }
}