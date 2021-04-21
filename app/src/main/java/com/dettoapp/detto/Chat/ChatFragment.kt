package com.dettoapp.detto.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentChatBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding, ChatRepository>() {

    private lateinit var chatAdapter: ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun liveDataObservers() {
        observeWithLiveData(viewModel.chatMessages, onSuccess = {
            chatAdapter.differ.submitList(it)
            lifecycleScope.launch {
                delay(100)
                binding.chatRecyclerView.smoothScrollToPosition(getRecyclerViewScrollPosition())
            }
        })

        observeWithLiveData(viewModel.chatMessageEvent, onLoading = {
            binding.chatProgressBar.visibility = View.VISIBLE
            //binding.sendChatButton.isEnabled = true
        }, onSuccess = {
            binding.sendMessageField.text.clear()
            binding.chatProgressBar.visibility = View.GONE
            //binding.sendChatButton.isEnabled = true
        }, onError = {
            baseActivity.showErrorSnackMessage(it)
            baseActivity.closeKeyBoard(requireView())
           // binding.sendChatButton.isEnabled = true
        })
    }

    private fun initialise() {
        chatAdapter = ChatAdapter(Utility.STUDENT.uid)

        binding.chatRecyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                lifecycleScope.launch {
                    delay(100)
                    binding.chatRecyclerView.smoothScrollToPosition(getRecyclerViewScrollPosition())
                }
            }
        }


        binding.sendChatButton.setOnClickListener {
            sendMessageToServer()
        }

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun sendMessageToServer() {
        viewModel.sendMessage(binding.sendMessageField.text.toString())
    }

    private fun getRecyclerViewScrollPosition() = if (chatAdapter.itemCount == 0)
        0
    else
        chatAdapter.itemCount - 1

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