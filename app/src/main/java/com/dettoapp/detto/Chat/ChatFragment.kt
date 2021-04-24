package com.dettoapp.detto.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.databinding.FragmentChatBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment(private val classroom: Classroom, private val name: String, private val userID: String) : BaseFragment<ChatViewModel, FragmentChatBinding, ChatRepository>() {

    private lateinit var chatAdapter: ChatAdapter

    override fun getBaseOnCreate() {
        viewModel.initialise(classroom, name, userID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun liveDataObservers() {
        observeWithLiveData(viewModel.chatMessages, onSuccess = {
            binding.chatProgressBar.visibility = View.GONE
            chatAdapter.differ.submitList(it)
            lifecycleScope.launch {
                delay(100)
                binding.chatRecyclerView.smoothScrollToPosition(getRecyclerViewScrollPosition())
            }
        })

        observeWithLiveData(viewModel.chatMessageEvent, onLoading = {
            binding.chatProgressBar.visibility = View.VISIBLE
            enableHideChatSendButton(false)
        }, onSuccess = {
            if (it.isEmpty())
                binding.sendMessageField.text.clear()
            binding.chatProgressBar.visibility = View.GONE
            enableHideChatSendButton()
        }, onError = {
            binding.chatProgressBar.visibility = View.GONE
            enableHideChatSendButton()
            when (it) {
                Constants.CHAT_DISCONNECTED -> {
                    binding.disconnecttedChatBox.visibility = View.VISIBLE
                }
                else -> {
                    baseActivity.showErrorSnackMessage(it)
                    baseActivity.closeKeyBoard(requireView())
                }
            }

        }, onConfirm = {
            binding.chatProgressBar.visibility = View.GONE
            binding.disconnecttedChatBox.visibility = View.GONE
        })
    }


    private fun enableHideChatSendButton(isEnabled: Boolean = true) {
        if (!isEnabled) {
            binding.sendChatButton.alpha = 0.3f
            binding.sendChatButton.isEnabled = false
        } else {
            binding.sendChatButton.alpha = 1f
            binding.sendChatButton.isEnabled = true
        }
    }

    private fun initialise() {
        chatAdapter = ChatAdapter(userID)

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


        binding.disconnecttedChatBox.setOnClickListener {
            binding.chatProgressBar.visibility = View.VISIBLE
            viewModel.reconnect()
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
        return ChatRepository(ChatServiceProvider(), DatabaseDetto.getInstance(requireContext().applicationContext).chatMessageDAO)
    }

    override fun onBaseDestroy() {
        viewModel.closeConnection()
    }
}