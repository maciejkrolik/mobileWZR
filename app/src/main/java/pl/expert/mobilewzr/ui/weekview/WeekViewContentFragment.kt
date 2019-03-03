package pl.expert.mobilewzr.ui.weekview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.data.model.WeekViewItem
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import javax.inject.Inject

class WeekViewContentFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentWeekViewContentBinding

    private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)

        val recyclerAdapter = WeekViewRecyclerAdapter(listOfWeekViewItems)

        binding.weekViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(WeekViewViewModel::class.java)

        val weekNumber: Int
        if (arguments?.getInt("argWeekNumber") != null) {
            weekNumber = arguments?.getInt("argWeekNumber") as Int
        } else {
            throw KotlinNullPointerException("Week number argument was null.")
        }

        weekViewViewModel.getSubjects(weekNumber).observe(viewLifecycleOwner,
            Observer<List<WeekViewItem>> { listOfItems ->
                if (listOfItems != null) {
                    listOfWeekViewItems.clear()
                    listOfWeekViewItems.addAll(listOfItems)
                }

                binding.weekViewProgressBar.visibility = View.GONE
                binding.weekViewRecyclerView.visibility = View.VISIBLE
            })
    }
}
