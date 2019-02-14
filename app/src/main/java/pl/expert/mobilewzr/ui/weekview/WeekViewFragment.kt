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
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.WeekViewItem
import pl.expert.mobilewzr.databinding.FragmentWeekViewBinding
import pl.expert.mobilewzr.util.CalendarUtils
import javax.inject.Inject

class WeekViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var weekViewViewModel: WeekViewViewModel

    private lateinit var binding: FragmentWeekViewBinding
    private lateinit var viewAdapter: WeekViewAdapter

    private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewBinding.inflate(inflater, container, false)

        viewAdapter = WeekViewAdapter(listOfWeekViewItems)

        binding.weekViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        if (CalendarUtils.getWeekNumber() == 0) {
            activity?.title = getString(R.string.weekA)
        } else {
            activity?.title = getString(R.string.weekB)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        weekViewViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(WeekViewViewModel::class.java)

        weekViewViewModel.getSubjects("S22-31").observe(viewLifecycleOwner,
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
