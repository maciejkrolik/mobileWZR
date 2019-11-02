package pl.expert.mobilewzr.ui.timetable.dayview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentDayViewContentBinding
import pl.expert.mobilewzr.domain.domainmodel.TimetableDataHolder
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.ui.timetable.TimetableContentBaseFragment
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation
import pl.expert.mobilewzr.ui.timetable.TimetableViewModel
import pl.expert.mobilewzr.util.ResourceState

class DayViewContentFragment : TimetableContentBaseFragment(), DayViewRecyclerAdapter.OnSubjectListener {

    private lateinit var binding: FragmentDayViewContentBinding
    private lateinit var viewModel: TimetableViewModel
    private lateinit var timetableDataHolder: TimetableDataHolder

    private val subjectItems: MutableList<SubjectItem> = mutableListOf()
    private val weekNumber by lazy { arguments?.getInt("argWeekNumber", 0)!! }
    private val weekDayNumber by lazy { arguments?.getInt("argWeekDayNumber", 0)!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDayViewContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(TimetableViewModel::class.java)

        viewModel.timetableDataHolder.observe(viewLifecycleOwner, Observer { resourceState ->
            when (resourceState) {
                is ResourceState.Success -> {
                    this.timetableDataHolder = resourceState.data!!

                    val dayViewItems = viewModel.getDayViewItems(weekNumber, weekDayNumber)
                    if (dayViewItems.isNotEmpty()) {
                        this.subjectItems.clear()
                        this.subjectItems.addAll(dayViewItems)

                        binding.dayViewProgressBar.visibility = View.GONE
                        binding.dayViewRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.dayViewProgressBar.visibility = View.GONE
                        binding.dayViewInfoText.visibility = View.VISIBLE
                    }
                }
            }

        })
    }

    private fun setupRecyclerView() {
        val recyclerAdapter = DayViewRecyclerAdapter(subjectItems, this)
        binding.dayViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    override fun onSubjectClick(position: Int) {
        val subjectIndex = getSubjectIndexBasedOnPositionInDayView(position)
        if (timetableViewLocation == TimetableViewLocation.MY_TIMETABLE) {
            navigateToEditFragment(subjectIndex)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_as_my_timetable -> {
                viewModel.replaceSubjectsInDb()
                putMyGroupIdIntoSharedPref(viewModel.groupId)
                showToast(viewModel.groupId)
                return true
            }
            R.id.add_new_subject -> {
                val args = Bundle()
                args.putInt("argSubjectIndex", -1)
                args.putInt("argWeekNumber", weekNumber)
                args.putInt("argWeekDayNumber", weekDayNumber)
                findNavController().navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
                return true
            }
            R.id.choose_view -> {
                chooseView(viewModel.groupId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSubjectIndexBasedOnPositionInDayView(position: Int): Int {
        val subjectIndex: Int
        when (weekNumber) {
            0 -> {
                subjectIndex = when (weekDayNumber) {
                    0 -> timetableDataHolder.weekA.mondaySubjects[position].index
                    1 -> timetableDataHolder.weekA.tuesdaySubjects[position].index
                    2 -> timetableDataHolder.weekA.wednesdaySubjects[position].index
                    3 -> timetableDataHolder.weekA.thursdaySubjects[position].index
                    4 -> timetableDataHolder.weekA.fridaySubjects[position].index
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDayNumber")
                }
            }
            1 -> {
                subjectIndex = when (weekDayNumber) {
                    0 -> timetableDataHolder.weekB.mondaySubjects[position].index
                    1 -> timetableDataHolder.weekB.tuesdaySubjects[position].index
                    2 -> timetableDataHolder.weekB.wednesdaySubjects[position].index
                    3 -> timetableDataHolder.weekB.thursdaySubjects[position].index
                    4 -> timetableDataHolder.weekB.fridaySubjects[position].index
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
        findNavController().navigate(R.id.action_my_timetable_view_fragment_to_editViewFragment, args)
    }

}
