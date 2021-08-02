package com.example.localchat.ui.home.selectcontact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.localchat.R
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.InflateCreateNewGroupItemBinding
import com.example.localchat.databinding.InflateSelectContactItemBinding
import kotlinx.android.synthetic.main.inflate_chat_home_item.view.*

class SelectContactAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectContactList: ArrayList<UserEntity?> = ArrayList()
    private var onItemClickListener: OnItemClick? = null

    init {
        this.selectContactList.add(0, null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val inflateSelectContactItemBinding = DataBindingUtil.bind<ViewDataBinding>(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_select_contact_item, parent, false)
            ) as InflateSelectContactItemBinding
            SelectContactViewHolder(inflateSelectContactItemBinding)
        } else {
            val inflateCreateNewGroupItemBinding = DataBindingUtil.bind<ViewDataBinding>(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_create_new_group_item, parent, false)
            ) as InflateCreateNewGroupItemBinding
            return CreateNewGroupViewHolder(inflateCreateNewGroupItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelectContactViewHolder -> {
                holder.bind(selectContactList[holder.adapterPosition])
            }
            is CreateNewGroupViewHolder -> {
                holder.bind()
            }
        }
        holder.itemView.viewDivider.visibility = if (holder.adapterPosition == itemCount - 1) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (selectContactList[position] == null) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return selectContactList.size
    }

    fun addList(chatHomeList: ArrayList<UserEntity>) {
        clearList()
        this.selectContactList.add(0, null)
        this.selectContactList.addAll(chatHomeList)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.selectContactList.clear()
        notifyDataSetChanged()
    }

    inner class SelectContactViewHolder(private var inflateSelectContactItemBinding: InflateSelectContactItemBinding) :
        RecyclerView.ViewHolder(inflateSelectContactItemBinding.root) {
        fun bind(chatHomeModel: UserEntity?) {
            inflateSelectContactItemBinding.imgChatIcon.let { iv ->
                Glide.with(iv.context).load("")
                    .apply(
                        RequestOptions().circleCrop().error(R.drawable.ic_default_profile)
                    )
                    .into(iv)
            }
            inflateSelectContactItemBinding.tvChatTitle.text = chatHomeModel?.userName
            inflateSelectContactItemBinding.tvChatLastMessage.text = "Hello hi Local DB data"

            inflateSelectContactItemBinding.rootLayoutChatItem.setOnClickListener {
                onItemClickListener?.onChatItemClick(chatHomeModel)
            }
        }
    }

    inner class CreateNewGroupViewHolder(private var inflateCreateNewGroupItemBinding: InflateCreateNewGroupItemBinding) :
        RecyclerView.ViewHolder(inflateCreateNewGroupItemBinding.root) {
        fun bind() {
            inflateCreateNewGroupItemBinding.imgChatIcon.let { iv ->
                Glide.with(iv.context).load("")
                    .apply(
                        RequestOptions().circleCrop().error(R.drawable.ic_default_create_grp)
                    )
                    .into(iv)
            }
            inflateCreateNewGroupItemBinding.tvChatTitle.text = "New Group"
            inflateCreateNewGroupItemBinding.rootLayoutCreateNewGrp.setOnClickListener {
                onItemClickListener?.onNewCreateGroupItemClick()
            }
        }
    }

    fun setOnItemClickListener(clickListener: OnItemClick) {
        this.onItemClickListener = clickListener
    }

    interface OnItemClick {
        fun onChatItemClick(chatHomeModel: UserEntity?)
        fun onNewCreateGroupItemClick()
    }
}