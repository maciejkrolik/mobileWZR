package pl.expert.mobilewzr.ui.timetable.dayview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentDayViewContentBinding
import pl.expert.mobilewzr.domain.domainmodel.DayViewDataHolder
import pl.expert.mobilewzr.domain.domainmodel.DayViewItem
import pl.expert.mobilewzr.ui.timetable.TimetableContentBaseFragment
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation

class DayViewContentFragment : TimetableContentBaseFragment(), DayViewRecyclerAdapter.OnSubjectListener {

    private lateinit var binding: FragmentDayViewContentBinding
    private lateinit var dayViewViewModel: DayViewViewModel
    private lateinit var dayViewDataHolder: DayViewDataHolder
    private var weekNumber: Int? = null
    private var weekDayNumber: Int? = null

    private val dayViewItems: MutableList<DayViewItem> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDayViewContentBinding.inflate(inflater, container, false)

        weekNumber = arguments?.getInt("argWeekNumber")!!
        weekDayNumber = arguments?.getInt("argWeekDayNumber")!!

        val recyclerAdapter = DayViewRecyclerAdapter(dayViewItems, this)
        binding.dayViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayViewViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory).get(DayViewViewModel::class.java)

        dayViewViewModel.getDayViewItems(weekNumber as Int, weekDayNumber as Int).observe(viewLifecycleOwner,
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

        dayViewViewModel.getDayViewDataHolder().observe(viewLifecycleOwner,
            Observer { dayViewDataHolder ->
                if (dayViewDataHolder != null) {
                    this.dayViewDataHolder = dayViewDataHolder
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_as_my_timetable -> {
                dayViewViewModel.replaceSubjectsInDb()
                putIdOfAGroupSavedInDbIntoSharedPref(dayViewViewModel.groupId)
                showToast(dayViewViewModel.groupId)
                return true
            }
            R.id.add_new_subject -> {
                val args = Bundle()
                args.putInt("argSubjectIndex", -1)
                args.putInt("argWeekNumber", weekNumber ?: 0)
                args.putInt("argWeekDayNumber", weekDayNumber ?: 0)
                Navigation.findNavController(view!!)
                    .navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSubjectClick(position: Int) {
        val subjectIndex = getSubjectIndexBasedOnPositionInDayView(position)
        if (timetableViewLocation == TimetableViewLocation.MY_TIMETABLE) {
            navigateToEditFragment(subjectIndex)
        }
    }

    private fun getSubjectIndexBasedOnPositionInDayView(position: Int): Int {
        val subjectIndex: Int
        when (weekNumber) {
            0 -> {
                subjectIndex = when (weekDayNumber) {
                    0 -> dayViewDataHolder.AWeek.mondaySubjects[position].index
                    1 -> dayViewDataHolder.AWeek.tuesdaySubjects[position].index
                    2 -> dayViewDataHolder.AWeek.wednesdaySubjects[position].index
                    3 -> dayViewDataHolder.AWeek.thursdaySubjects[position].index
                    4 -> dayViewDataHolder.AWeek.fridaySubjects[position].index
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDayNumber")
                }
            }
            1 -> {
                subjectIndex = when (weekDayNumber) {
                    0 -> dayViewDataHolder.BWeek.mondaySubjects[position].index
                    1 -> dayViewDataHolder.BWeek.tuesdaySubjects[position].index
                    2 -> dayViewDataHolder.BWeek.wednesdaySubjects[position].index
                    3 -> dayViewDataHolder.BWeek.thursdaySubjects[position].index
                    4 -> dayViewDataHolder.BWeek.fridaySubjects[position].index
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDayNumber")
                }
            }
            else -> throw IllegalArgumentException("Unknown week number: $weekNumber")
        }
        return subjectIndex
    }

    private fun navigateToEditFragment(subjectIndex: Int) {
        val args = Bundle()
        args.putInt("argSubjectIndex", subjectIndex)
        Navigation.findNavController(view!!).navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
    }

}
