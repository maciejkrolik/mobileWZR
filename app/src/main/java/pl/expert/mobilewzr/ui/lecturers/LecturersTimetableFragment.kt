package pl.expert.mobilewzr.ui.lecturers

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_lecturers_timetable.*
import kotlinx.android.synthetic.main.toolbar.view.*
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentLecturersTimetableBinding
import pl.expert.mobilewzr.ui.BaseInjectedFragment

class LecturersTimetableFragment : BaseInjectedFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LecturersTimetableViewModel::class.java)
    }

    private lateinit var binding: FragmentLecturersTimetableBinding
    private lateinit var groupId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLecturersTimetableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        getDataFromSharedPrefs()
        setOnClickListeners()
        observeData()
    }

    private fun setTitle() {
        toolbar.toolbarTitle.text = getString(R.string.lecturers)
    }

    private fun getDataFromSharedPrefs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        groupId = sharedPref.getString("prefIdOfAGroupSavedInDb", "")!!
    }

    private fun setOnClickListeners() {
        getDataButton.setOnClickListener {
            viewModel.getLecturersSubjects()
        }
    }

    private fun observeData() {
        viewModel.subjects.observe(viewLifecycleOwner, Observer { subjects ->
            if (subjects != null) {
                lecturersTimetableGrid.setSubjects(subjects)
            }
        })
    }

}