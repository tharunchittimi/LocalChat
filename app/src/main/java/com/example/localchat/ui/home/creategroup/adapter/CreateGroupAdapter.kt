package com.example.localchat.ui.home.creategroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.localchat.R
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.InflateCreateGroupGridItemBinding

class CreateGroupAdapter : RecyclerView.Adapter<CreateGroupAdapter.CreateGroupViewHolder>() {

    private var selectContactList: ArrayList<UserEntity?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateGroupViewHolder {
        val inflateCreateGroupGridItemBinding = DataBindingUtil.bind<ViewDataBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_create_group_grid_item, parent, false)
        ) as InflateCreateGroupGridItemBinding
        return CreateGroupViewHolder(inflateCreateGroupGridItemBinding)
    }

    override fun onBindViewHolder(holder: CreateGroupViewHolder, position: Int) {
        holder.bind(selectContactList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return selectContactList.size
    }

    fun addList(chatHomeList: java.util.ArrayList<UserEntity?>) {
        clearList()
        this.selectContactList.addAll(chatHomeList)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.selectContactList.clear()
        notifyDataSetChanged()
    }

    inner class CreateGroupViewHolder(private var inflateCreateGroupGridItemBinding: InflateCreateGroupGridItemBinding) :
        RecyclerView.ViewHolder(inflateCreateGroupGridItemBinding.root) {
        fun bind(chatHomeModel: UserEntity?) {
            inflateCreateGroupGridItemBinding.imgChatIcon.let { iv ->
                Glide.with(iv.context).load("")
                    .apply(
                        RequestOptions().circleCrop().error(R.drawable.ic_default_profile)
                    )
                    .into(iv)
            }
            inflateCreateGroupGridItemBinding.tvChatTitle.text = chatHomeModel?.userName

        }
    }

    fun getItemsSize(): Int {
        return selectContactList.size
    }

}