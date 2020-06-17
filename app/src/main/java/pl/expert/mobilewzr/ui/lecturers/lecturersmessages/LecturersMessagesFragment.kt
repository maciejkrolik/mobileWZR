package pl.expert.mobilewzr.ui.lecturers.lecturersmessages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_lecturers_messages.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentLecturersMessagesBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.ResourceState
import pl.expert.mobilewzr.util.setAttrsAndShow

class LecturersMessagesFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LecturersMessagesViewModel::class.java)
    }

    private lateinit var binding: FragmentLecturersMessagesBinding
    private lateinit var recyclerAdapter: LecturersMessagesRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLecturersMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.groupsState.value = ResourceState.Idle()

        setTitle()
        setupRecyclerView()
        setOnClickListeners()
        setupDataBinding()
        observeData()
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.messages)
    }

    private fun setupRecyclerView() {
        recyclerAdapter = LecturersMessagesRecyclerAdapter {
            it.groupId?.let { groupId ->
                findNavController().navigate(
                    LecturersMessagesFragmentDirections.actionLecturersMessagesFragmentToThreadFragment(
                        groupId
                    )
                )
            }
        }
        threadsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    private fun setOnClickListeners() {
        addThread.setOnClickListener {
            viewModel.loadGroupPicker()
        }
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun observeData() {
        observeMessagesState()
        observeGroupsState()
    }

    private fun observeMessagesState() {
        viewModel.threadsState.observe(viewLifecycleOwner, Observer { threadsState ->
            when (threadsState) {
                is ResourceState.Success -> {
                    threadsState.data?.let {
                        recyclerAdapter.submitList(it)
                    }
                }
                is ResourceState.Error -> {
                    Toast.makeText(requireContext(), threadsState.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    private fun observeGroupsState() {
        viewModel.groupsState.observe(viewLifecycleOwner, Observer { groupState ->
            when (groupState) {
                is ResourceState.Success -> {
                    val groups = groupState.data?.toTypedArray()
                    var selectedItemId = -1
                    groups?.let {
                        val alertDialog = AlertDialog.Builder(requireContext()).apply {
                            setTitle(R.string.choose_group)
                            setSingleChoiceItems(groups, -1) { _, item ->
                                selectedItemId = item
                            }
                            setPositiveButton(R.string.ok) { _, _ ->
                                val selectedGroup = groups[selectedItemId]
                                findNavController().navigate(
                                    LecturersMessagesFragmentDirections.actionLecturersMessagesFragmentToThreadFragment(
                                        selectedGroup
                                    )
                                )
                            }
                            setNegativeButton(R.string.cancel) { dialog, _ ->
                                dialog.dismiss()
                            }
                        }.create()
                        alertDialog.setAttrsAndShow()
                    }
                }
                is ResourceState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cannot_send_a_message_right_now),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

}