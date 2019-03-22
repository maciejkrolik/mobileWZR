package pl.expert.mobilewzr.ui.timetableviews.dayview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.dto.DayViewItem
import pl.expert.mobilewzr.databinding.FragmentDayViewContentBinding
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewContentBaseFragment
import javax.inject.Inject

class DayViewContentFragment : TimetableViewContentBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDayViewContentBinding
    private lateinit var dayViewViewModel: DayViewViewModel
    private var weekNumber: Int? = null
    private var weekDay: Int? = null

    private val dayViewItems: MutableList<DayViewItem> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDayViewContentBinding.inflate(inflater, container, false)

        weekNumber = arguments?.getInt("argWeekNumber")!!
        weekDay = arguments?.getInt("argWeekDayNumber")!!

        val recyclerAdapter = DayViewRecyclerAdapter(dayViewItems)
        binding.dayViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dayViewViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory).get(DayViewViewModel::class.java)

        dayViewViewModel.getDayViewItems(weekNumber as Int, weekDay as Int).observe(viewLifecycleOwner,
            Observer<List<DayViewItem>> { dayViewItems ->
                if (dayViewItems != null && !dayViewItems.isEmpty()) {
                    this.dayViewItems.clear()
                    this.dayViewItems.addAll(dayViewItems)

                    binding.dayViewProgressBar.visibility = View.GONE
                    binding.dayViewRecyclerView.visibility = View.VISIBLE
                } else {
                    binding.dayViewProgressBar.visibility = View.GONE
                    binding.dayViewInfoText.visibility = View.VISIBLE
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.set_as_my_timetable -> {
                dayViewViewModel.replaceSubjectsInDb()
                putIdOfAGroupSavedInDbIntoSharedPref(dayViewViewModel.groupId)
                showToast(dayViewViewModel.groupId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
