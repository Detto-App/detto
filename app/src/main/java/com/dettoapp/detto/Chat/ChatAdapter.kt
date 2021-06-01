package com.dettoapp.detto.Chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.databinding.ChatCellReceiveBinding
import com.dettoapp.detto.databinding.ChatCellSendBinding

class ChatAdapter(private val userID: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.chatid == newItem.chatid
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].senderid == userID)
            0
        else 1
    }


    inner class ChatSendViewHolder(val binding: ChatCellSendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.messageTextSend.text = chatMessage.message
            binding.sendTime.text = chatMessage.time
        }
    }

    inner class ChatReceiveViewHolder(val binding: ChatCellReceiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.receiveTime.text = chatMessage.time
            binding.senderReceive.text = chatMessage.name
            binding.messageTextReceive.text = chatMessage.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> ChatSendViewHolder(ChatCellSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> ChatReceiveViewHolder(
                ChatCellReceiveBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as ChatSendViewHolder).bind(differ.currentList[position])
            else -> (holder as ChatReceiveViewHolder).bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}