package com.dettoapp.detto.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentChatBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding, ChatRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.chatRecyclerView)

        val d = ChatAdapter(Utility.STUDENT.uid)
        rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = d
        }



        observeWithLiveData(viewModel.chatMessages, onSuccess = {
            d.differ.submitList(it)
        })
    }

    override fun getViewModelClass(): Class<ChatViewModel> {
        return ChatViewModel::class.java
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): ChatRepository {
        return ChatRepository(ChatServiceProvider())
    }

    override fun onBaseDestroy() {
        viewModel.closeConnection()
    }
}

//val x = arrayListOf<ChatMessage>(
//        ChatMessage("hey", "Vishwa - 1DS17CS123", "6:00 pm", "15536", Utility.createID()),
//        ChatMessage("hi", "Vishwa - 1DS17CS123", "6:00 pm", Utility.STUDENT.uid, Utility.createID()),
//        ChatMessage("listen", "Vishwa - 1DS17CS123", "6:00 pm", "15536", Utility.createID()),
//        ChatMessage("wassup", "Vishwa - 1DS17CS123", "6:00 pm", "15536", Utility.createID()),
//        ChatMessage("nm", "Vishwa - 1DS17CS123", "6:00 pm", Utility.STUDENT.uid, Utility.createID()),
//        ChatMessage("ugt", "Vishwa - 1DS17CS123", "6:00 pm", Utility.STUDENT.uid, Utility.createID()),
//        ChatMessage("idk man", "Vishwa - 1DS17CS123", "6:00 pm", "15536", Utility.createID()),
//        ChatMessage("idk life is boring", "Vishwa - 1DS17CS123", "6:00 pm", "15536", Utility.createID()),
//)