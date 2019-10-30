package pl.expert.mobilewzr.ui.lecturers.lecturerstimetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_container_day_view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.CalendarUtils

class LecturersTimetableContainerFragment : BaseInjectedFragment() {

    private lateinit var viewModel: LecturersTimetableViewModel
    private lateinit var lecturerName: String

    private val weekNumber = CalendarUtils.getWeekNumber()
    private val args: LecturersTimetableContainerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container_lecturers_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lecturerName = args.lecturerName
        setTitle()
        setupViewModel()
        setupTabLayout()
        setupPagerAdapter()
    }

    private fun setTitle() {
        (activity as AppCompatActivity).toolbar.toolbarTitle.text = lecturerName
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory).get(LecturersTimetableViewModel::class.java)
        viewModel.getLecturersSubjectsFromDb(lecturerName)
    }

    private fun setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupPagerAdapter() {
        viewPager.adapter = LecturersTimetablePagerAdapter(context, childFragmentManager)
        viewPager.currentItem = weekNumber
    }

}