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
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.databinding.FragmentWeekViewContentBinding
import javax.inject.Inject

class WeekViewContentFragment : Fragment(), WeekViewRecyclerAdapter.OnSubjectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentWeekViewContentBinding
    var weekNumber: Int? = null

    private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()
    private val listOfSubjects: MutableList<Subject> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContentBinding.inflate(inflater, container, false)

        val recyclerAdapter = WeekViewRecyclerAdapter(listOfWeekViewItems, this)

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

        if (arguments?.getInt("argWeekNumber") != null) {
            weekNumber = arguments?.getInt("argWeekNumber") as Int
        } else {
            throw KotlinNullPointerException("Week number argument was null.")
        }

        weekViewViewModel.getWeekViewItems(weekNumber as Int).observe(viewLifecycleOwner,
            Observer<List<WeekViewItem>> { listOfWeekViewItems ->
                if (listOfWeekViewItems != null) {
                    this.listOfWeekViewItems.clear()
                    this.listOfWeekViewItems.addAll(listOfWeekViewItems)
                }

                binding.weekViewProgressBar.visibility = View.GONE
                binding.weekViewRecyclerView.visibility = View.VISIBLE
            })

        weekViewViewModel.getSubjects().observe(viewLifecycleOwner,
            Observer<List<Subject>> { listOfSubjects ->
                if (listOfSubjects != null) {
                    this.listOfSubjects.clear()
                    this.listOfSubjects.addAll(listOfSubjects)
                }
            })
    }

    override fun onSubjectClick(position: Int, dayOfWeek: Int) {
        val csvIndex = listOfWeekViewItems[position].listOfSubjects[dayOfWeek].csvIndex
        if (csvIndex != -1) {
            Toast.makeText(context, listOfSubjects[csvIndex].description, Toast.LENGTH_SHORT).show()
        }
    }
}