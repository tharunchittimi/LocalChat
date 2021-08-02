package com.example.localchat.ui.home.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.localchat.R
import com.example.localchat.base.room.relationtables.UsersMessageRelationMain
import com.example.localchat.databinding.InflateChatHomeItemBinding
import com.example.localchat.ui.home.singlechat.SingleChatActivity.Companion.simpleDateFormat
import kotlinx.android.synthetic.main.inflate_chat_home_item.view.*


class ChatHomeAdapter : RecyclerView.Adapter<ChatHomeAdapter.ChatHomeViewHolder>() {

    private var chatList: ArrayList<UsersMessageRelationMain?> = ArrayList()
    private var onItemClickListener: OnItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHomeViewHolder {
        val inflateChatHomeItemBinding = DataBindingUtil.bind<ViewDataBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_chat_home_item, parent, false)
        ) as InflateChatHomeItemBinding
        return ChatHomeViewHolder(inflateChatHomeItemBinding)
    }

    override fun onBindViewHolder(holder: ChatHomeViewHolder, position: Int) {
        holder.bind(chatList[holder.adapterPosition])
        holder.itemView.viewDivider.visibility = if (holder.adapterPosition == itemCount - 1) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun addList(chatHomeList: List<UsersMessageRelationMain?>) {
        clearList()
        this.chatList.addAll(chatHomeList)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.chatList.clear()
        notifyDataSetChanged()
    }

    inner class ChatHomeViewHolder(private var inflateChatHomeItemBinding: InflateChatHomeItemBinding) :
        RecyclerView.ViewHolder(inflateChatHomeItemBinding.root) {
        fun bind(chatHomeModel: UsersMessageRelationMain?) {
            inflateChatHomeItemBinding.imgChatIcon.let { iv ->
                Glide.with(iv.context).load("")
                    .apply(
                        RequestOptions().circleCrop()
                            .error(if (chatHomeModel?.isGroup == 0) R.drawable.ic_default_profile else R.drawable.ic_default_group_profile)
                    )
                    .into(iv)
            }
            inflateChatHomeItemBinding.tvChatTitle.text =
                if (chatHomeModel?.isGroup == 1) chatHomeModel.groupName else chatHomeModel?.userName
            inflateChatHomeItemBinding.tvChatLastMessage.text =
                chatHomeModel?.lastMessage
            inflateChatHomeItemBinding.tvChatDate.text =
                if (chatHomeModel?.lastMessageDate != null) simpleDateFormat.format(
                    chatHomeModel.lastMessageDate
                ) else ""

            inflateChatHomeItemBinding.rootLayoutChatItem.setOnClickListener {
                onItemClickListener?.onChatItemClick(chatHomeModel)
            }
        }
    }

    fun setOnItemClickListener(clickListener: OnItemClick) {
        this.onItemClickListener = clickListener
    }

    interface OnItemClick {
        fun onChatItemClick(chatHomeModel: UsersMessageRelationMain?)
    }
}