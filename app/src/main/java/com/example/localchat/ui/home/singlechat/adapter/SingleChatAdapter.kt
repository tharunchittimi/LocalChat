package com.example.localchat.ui.home.singlechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localchat.R
import com.example.localchat.base.room.entities.MessagesEntity
import com.example.localchat.ui.home.singlechat.SingleChatActivity.Companion.simpleDateFormat
import com.example.localchat.ui.home.singlechat.SingleChatActivity.Companion.simpleTimeFormat
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.ui.home.singlechat.model.BaseWithHeader
import com.example.localchat.ui.home.singlechat.model.HeaderModel
import com.example.localchat.util.CHAT_DATE_HEADER
import com.example.localchat.util.OTHERS_CHAT
import com.example.localchat.util.SELF_CHAT
import com.example.localchat.util.StickHeaderItemDecoration
import kotlinx.android.synthetic.main.inflate_single_receive_chat_item.view.*
import kotlinx.android.synthetic.main.inflate_single_send_chat_item.view.*

class SingleChatAdapter(val currentLoggedInUserData: UserEntity?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickHeaderItemDecoration.StickyHeaderInterface {

    private var listener: Listener? = null
    private var arrayList: ArrayList<BaseWithHeader> = ArrayList()

    override fun getHeaderPositionForItem(itemPositio: Int): Int {
        var itemPosition = itemPositio
        var headerPosition = 0
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.inflate_chatting_sticky_header_item
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        val tvDateHeader: TextView? = header?.findViewById(R.id.tvDateHeader)
        if (arrayList[headerPosition] is HeaderModel) {
            val headerModel: HeaderModel = arrayList[headerPosition] as HeaderModel
            header?.visibility = View.VISIBLE
            tvDateHeader?.text = simpleDateFormat.format(headerModel.headerValue)
        } else {
            header?.visibility = View.GONE
        }
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return if (0 <= itemPosition)
            arrayList[itemPosition] is HeaderModel
        else {
            false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            OTHERS_CHAT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_single_receive_chat_item, parent, false)
                ReceiveViewHolder(view)
            }
            SELF_CHAT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_single_send_chat_item, parent, false)
                SendViewHolder(view)
            }
            CHAT_DATE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_chatting_sticky_header_item, parent, false)
                return ItemHeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_single_send_chat_item, parent, false)
                SendViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (val data = arrayList[position]) {
            is MessagesEntity -> {
                if (data.fromUserId == currentLoggedInUserData?.userId) {
                    SELF_CHAT
                } else {
                    OTHERS_CHAT
                }
            }
            is HeaderModel -> {
                CHAT_DATE_HEADER
            }
            else -> {
                -1
            }
        }
    }

    inner class SendViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    inner class ReceiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReceiveViewHolder -> {
                (arrayList[holder.adapterPosition] as? MessagesEntity)?.let {
                    holder.itemView.tvReceiveData?.text = it.message
                    holder.itemView.tvReceiveDataTime?.text =
                        "${simpleTimeFormat.format(it.dateAndTime)}"
                }
            }
            is SendViewHolder -> {
                (arrayList[holder.adapterPosition] as? MessagesEntity)?.let {
                    holder.itemView.tvSendData?.text = it.message
                    holder.itemView.tvSendDataTime?.text =
                        "${simpleTimeFormat.format(it.dateAndTime)}"
                }
            }
            is ItemHeaderViewHolder -> {
                (holder).bind(holder.adapterPosition)
            }
        }
    }

    inner class ItemHeaderViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(position: Int) {
            val headerModel: HeaderModel = arrayList[position] as HeaderModel
            tvDateHeader.text = simpleDateFormat.format(headerModel.headerValue)
        }

        private var tvDateHeader: TextView = rootView.findViewById(R.id.tvDateHeader)
    }

    fun addItems(list: ArrayList<MessagesEntity>) {
        clearList()
        this.arrayList.addAll(addHeaders(list))
        notifyDataSetChanged()
        listener?.mustScrollTOLast()
    }

    fun addSendMessage(sendModelData: MessagesEntity) {
        var lastHeader: String? = null
        val lastPosition: Int = arrayList.size - 1
        if (lastPosition >= 0) {
            lastHeader = "${simpleDateFormat.format(arrayList[lastPosition].sectionHeaderDate)}"
        }
        if (lastHeader != simpleDateFormat.format(sendModelData.dateAndTime)) {
            arrayList.add(HeaderModel(sendModelData.dateAndTime ?: 0L))
            notifyItemInserted(arrayList.size - 1)

        }
        arrayList.add(sendModelData)
        notifyItemInserted(arrayList.size - 1)

        listener?.mustScrollTOLast()
    }

    private fun addHeaders(list: ArrayList<MessagesEntity>): ArrayList<BaseWithHeader> {
        val listWithHeader: ArrayList<BaseWithHeader> = ArrayList()
        var headerDate: String? = ""
        for (message in list) {
            val date = simpleDateFormat.format(message.dateAndTime)
            if (headerDate != date) {
                headerDate = date
                listWithHeader.add(HeaderModel(message.dateAndTime ?: 0L))
            }
            listWithHeader.add(message)
        }
        return listWithHeader
    }

    fun clearList() {
        this.arrayList.clear()
        notifyDataSetChanged()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun scrollToLast()
        fun mustScrollTOLast()
    }
}