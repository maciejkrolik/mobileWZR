package pl.expert.mobilewzr.ui.lecturers.lecturerstimetable

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_container_day_view.tabLayout
import kotlinx.android.synthetic.main.fragment_container_day_view.viewPager
import kotlinx.android.synthetic.main.fragment_container_lecturers_timetable.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentContainerLecturersTimetableBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment
import pl.expert.mobilewzr.util.*

class LecturersTimetableContainerFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(LecturersTimetableViewModel::class.java)
    }

    private lateinit var binding: FragmentContainerLecturersTimetableBinding
    private lateinit var lecturerName: String

    private val weekNumber = CalendarUtils.getWeekNumber()
    private val args: LecturersTimetableContainerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentContainerLecturersTimetableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lecturers_timetable_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshLecturersTimetable -> {
                if (viewModel.subjectsState.value !is ResourceState.Loading) {
                    showConfirmationDialog()
                } else {
                    Toast.makeText(requireContext(), R.string.processing_in_progress, Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setMessage(R.string.refresh_lecturers_timetable_confirmation)
            setPositiveButton(R.string.yes) { _, _ ->
                getTimetableData()
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
        alertDialog.setAttrsAndShow()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lecturerName = args.lecturerName
        setTitle()
        setupViewModel()
        setupTabLayout()
        setupPagerAdapter()
        setOnClickListeners()

        viewModel.getLecturersSubjectsFromDb(lecturerName)
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = lecturerName
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupPagerAdapter() {
        viewPager.adapter = LecturersTimetablePagerAdapter(context, childFragmentManager)
        viewPager.currentItem = weekNumber
        tabLayout.addCurrentDayOrWeekIndicator(requireContext(), weekNumber)
    }

    private fun setOnClickListeners() {
        getDataButton.setOnClickListener {
            getTimetableData()
        }
    }

    private fun getTimetableData() {
        if (NetworkUtils.isNetworkAvailable(requireContext()))
            viewModel.getLecturersSubjects(lecturerName)
        else
            Toast.makeText(requireContext(), R.string.internet_connection_error, Toast.LENGTH_SHORT).show()
    }

}