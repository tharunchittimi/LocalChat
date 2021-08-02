package com.example.localchat.ui.home.newgroup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.localchat.MyApplication
import com.example.localchat.R
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.InflateNewGroupItemBinding
import kotlinx.android.synthetic.main.inflate_chat_home_item.view.*


class NewGroupAdapter : RecyclerView.Adapter<NewGroupAdapter.NewGroupViewHolder>() {

    private var selectContactList: ArrayList<UserEntity?> = ArrayList()
    private var onItemClickListener: OnItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewGroupViewHolder {
        val inflateNewGroupItemBinding = DataBindingUtil.bind<ViewDataBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_new_group_item, parent, false)
        ) as InflateNewGroupItemBinding
        return NewGroupViewHolder(inflateNewGroupItemBinding)
    }

    override fun onBindViewHolder(holder: NewGroupViewHolder, position: Int) {
        holder.bind(selectContactList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return selectContactList.size
    }

    fun addList(chatHomeList: ArrayList<UserEntity>) {
        clearList()
        this.selectContactList.addAll(chatHomeList)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.selectContactList.clear()
        notifyDataSetChanged()
    }

    inner class NewGroupViewHolder(private var inflateNewGroupItemBinding: InflateNewGroupItemBinding) :
        RecyclerView.ViewHolder(inflateNewGroupItemBinding.root) {
        fun bind(chatHomeModel: UserEntity?) {
            inflateNewGroupItemBinding.imgChatIcon.let { iv ->
                Glide.with(iv.context).load("")
                    .apply(
                        RequestOptions().circleCrop().error(R.drawable.ic_default_profile)
                    )
                    .into(iv)
            }
            inflateNewGroupItemBinding.tvChatTitle.text = chatHomeModel?.userName
            inflateNewGroupItemBinding.tvChatLastMessage.text = "Hello hi Local DB data"
            if (chatHomeModel?.isSelected == true) {
                inflateNewGroupItemBinding.ivSelectIcon.visibility = View.VISIBLE
                inflateNewGroupItemBinding.ivSelectIcon.background = ContextCompat.getDrawable(
                    MyApplication.getApplicationContext(),
                    R.drawable.icon_checked
                )
            } else {
                inflateNewGroupItemBinding.ivSelectIcon.visibility = View.GONE
            }
            inflateNewGroupItemBinding.rootLayoutChatItem.setOnClickListener {
                setOnItemCheck(chatHomeModel)
                onItemClickListener?.onChatItemClick(chatHomeModel)
            }
        }
    }

    private fun setOnItemCheck(chatHomeModel: UserEntity?) {
        for ((position, item) in selectContactList.withIndex()) {
            if (item is UserEntity && item.userId.equals(
                    chatHomeModel?.userId
                )
            ) {
                item.isSelected = item.isSelected != true
                notifyItemChanged(position)
            }
        }
    }

    fun getSelectedItemsCount(): Int {
        var totalOrders = 0
        for ((pos, i) in selectContactList.withIndex()) {
            if (i?.isSelected == true) {
                totalOrders++
            }
        }
        return totalOrders
    }

    fun getSelectedItemsData(): ArrayList<UserEntity> {
        val selectedItemsData = ArrayList<UserEntity>()
        for ((pos, i) in selectContactList.withIndex()) {
            if (i?.isSelected == true) {
                selectedItemsData.add(i)
            }
        }
        return selectedItemsData
    }

    fun getItemsSize(): Int {
        return selectContactList.size
    }

    fun setOnItemClickListener(clickListener: OnItemClick) {
        this.onItemClickListener = clickListener
    }

    interface OnItemClick {
        fun onChatItemClick(chatHomeModel: UserEntity?)
    }
}