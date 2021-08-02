package com.example.localchat.ui.home.selectcontact

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.localchat.R
import com.example.localchat.BR
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.ActivitySelectContactBinding
import com.example.localchat.ui.home.newgroup.NewGroupActivity
import com.example.localchat.ui.home.selectcontact.adapter.SelectContactAdapter
import com.example.localchat.ui.home.singlechat.SingleChatActivity
import com.example.localchat.util.BUNDLE_USER_DATA
import java.util.*

class SelectContactActivity :
    BaseActivity<ActivitySelectContactBinding, SelectContactView, SelectContactViewModel>(),
    SelectContactView {

    private var selectContactAdapter: SelectContactAdapter? = null

    override fun getContentView(): Int {
        return R.layout.activity_select_contact
    }

    override fun setViewModelClass(): Class<SelectContactViewModel> {
        return SelectContactViewModel::class.java
    }

    override fun getNavigator(): SelectContactView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.selectContactViewModel
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        selectContactAdapter = SelectContactAdapter()
        mViewDataBinding?.rvSelectContactsList?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = selectContactAdapter
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        selectContactAdapter?.setOnItemClickListener(object : SelectContactAdapter.OnItemClick {
            override fun onChatItemClick(chatHomeModel: UserEntity?) {
                val createGroupIntent =
                    Intent(this@SelectContactActivity, SingleChatActivity::class.java)
                createGroupIntent.putExtra(BUNDLE_USER_DATA, chatHomeModel)
                startActivity(createGroupIntent)
            }

            override fun onNewCreateGroupItemClick() {
                startActivity(Intent(this@SelectContactActivity, NewGroupActivity::class.java))
            }

        })
        addData()
    }

    private fun addData() {
        mViewModel?.addLocalDBContactsList(getCurrentLoggedInUserData()?.userId)
    }

    override fun updateData(selectContactList: List<UserEntity?>?) {
        mViewDataBinding?.toolBarSelectContact?.txtToolContactsCount?.text =
            "${selectContactList?.size} contacts"
        if (selectContactList?.size ?: 0 > 0) {
            selectContactList?.let {
                selectContactAdapter?.addList(it as ArrayList<UserEntity>)
                mViewDataBinding?.rvSelectContactsList?.visibility = View.VISIBLE
            }
        } else {
            mViewDataBinding?.llvNoData?.visibility = View.VISIBLE
        }
    }

    private fun setUpToolBar() {
        mViewDataBinding?.toolBarSelectContact?.let {
            it.imgToolLeft.visibility = View.VISIBLE
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.txtToolTitle.text = "SELECT CONTACT"
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
        }
    }
}