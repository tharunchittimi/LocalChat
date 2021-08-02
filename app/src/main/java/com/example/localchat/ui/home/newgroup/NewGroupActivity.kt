package com.example.localchat.ui.home.newgroup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.localchat.R
import com.example.localchat.BR
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.ActivityNewGroupBinding
import com.example.localchat.ui.home.creategroup.CreateGroupActivity
import com.example.localchat.ui.home.newgroup.adapter.NewGroupAdapter
import com.example.localchat.util.BUNDLE_USERS_SELECTED
import com.example.localchat.util.BUNDLE_USER_DATA
import java.util.ArrayList

class NewGroupActivity : BaseActivity<ActivityNewGroupBinding, NewGroupView, NewGroupViewModel>(),
    NewGroupView {

    private var newGroupAdapter: NewGroupAdapter? = null

    override fun getContentView(): Int {
        return R.layout.activity_new_group
    }

    override fun setViewModelClass(): Class<NewGroupViewModel> {
        return NewGroupViewModel::class.java
    }

    override fun getNavigator(): NewGroupView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.newGroupViewModel
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        setUpRecyclerView()
        setViewClickListeners()
    }

    private fun setViewClickListeners() {
        mViewDataBinding?.btnCreateGrp?.setOnClickListener {
            val selectedItemsCount = newGroupAdapter?.getSelectedItemsCount()
            val selectedItemsData = newGroupAdapter?.getSelectedItemsData()
            if (selectedItemsCount ?: 0 <= 0) {
                showMessage("At least 1 contact must be selected")
            } else {
                val createGroupIntent = Intent(this, CreateGroupActivity::class.java)
                createGroupIntent.putExtra(BUNDLE_USER_DATA, selectedItemsData)
                createGroupIntent.putExtra(BUNDLE_USERS_SELECTED, selectedItemsCount)
                startActivity(createGroupIntent)
            }
        }
    }

    private fun setUpRecyclerView() {
        newGroupAdapter = NewGroupAdapter()
        mViewDataBinding?.rvNewGroupList?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = newGroupAdapter
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        newGroupAdapter?.setOnItemClickListener(object : NewGroupAdapter.OnItemClick {
            override fun onChatItemClick(chatHomeModel: UserEntity?) {
                mViewDataBinding?.toolBarNewGroup?.txtToolContactsCount?.text =
                    if (newGroupAdapter?.getSelectedItemsCount() ?: 0 > 0) {
                        "${newGroupAdapter?.getSelectedItemsCount()} of ${newGroupAdapter?.getItemsSize()} selected"
                    } else {
                        "Add Participants"
                    }
            }
        })
        addData()
    }

    private fun addData() {
        mViewModel?.addLocalDBContactsList(getCurrentLoggedInUserData()?.userId)
    }

    override fun updateData(selectContactList: List<UserEntity?>?) {
        if (selectContactList?.size ?: 0 > 0) {
            selectContactList?.let {
                newGroupAdapter?.addList(it as ArrayList<UserEntity>)
                mViewDataBinding?.grpRvItem?.visibility = View.VISIBLE
            }
        } else {
            mViewDataBinding?.llvNoData?.visibility = View.VISIBLE
        }
    }

    private fun setUpToolBar() {
        mViewDataBinding?.toolBarNewGroup?.let {
            it.imgToolLeft.visibility = View.VISIBLE
            it.txtToolContactsCount.visibility = View.VISIBLE
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.txtToolTitle.text = "NEW GROUP"
            it.txtToolContactsCount.text = "Add Participants"
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
        }
    }
}