package pl.expert.mobilewzr.ui.common.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_thread.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentThreadBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState

class ThreadFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ThreadViewModel::class.java)
    }

    private lateinit var binding: FragmentThreadBinding
    private lateinit var recyclerAdapter: ThreadRecyclerAdapter
    private val args: ThreadFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        setupRecyclerView()
        setOnClickListeners()
        setupDataBinding()
        observeData()

        viewModel.getThreadMessages(args.groupId)
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = args.groupId
    }

    private fun setupRecyclerView() {
        recyclerAdapter = ThreadRecyclerAdapter().apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    messagesRecyclerView.scrollToPosition(positionStart)
                }
            })
        }
        messagesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
            adapter = recyclerAdapter
        }
    }

    private fun setOnClickListeners() {
        sendButton.setOnClickListener {
            val message = messageContentEditText.text.toString()
            if (message.isBlank()) {
                Toast.makeText(requireContext(), R.string.message_cannot_be_empty, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sendMessage(messageContentEditText.text.toString(), args.groupId)
            }
        }
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun observeData() {
        viewModel.sendMessageState.observe(viewLifecycleOwner, Observer { sendMessageState ->
            when (sendMessageState) {
                is ResourceState.Success -> {
                    messageContentEditText.text.clear()
                    viewModel.getThreadMessages(args.groupId)
                }
                is ResourceState.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.error_while_sending_message), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

        viewModel.getMessagesState.observe(viewLifecycleOwner, Observer { getMessagesState ->
            when (getMessagesState) {
                is ResourceState.Success -> {
                    getMessagesState.data?.let {
                        recyclerAdapter.submitList(it)
                        messagesRecyclerView.scrollToPosition(it.size - 1)
                    }
                }
                is ResourceState.Error -> {
                    Toast.makeText(requireContext(), getMessagesState.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

}