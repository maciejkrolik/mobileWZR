package pl.expert.mobilewzr.ui.timetableviews.editview

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentEditViewBinding
import pl.expert.mobilewzr.ui.timetableviews.weekview.WeekViewViewModel
import javax.inject.Inject

class EditViewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val editViewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(EditViewViewModel::class.java)
    }
    private val weekViewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(WeekViewViewModel::class.java)
    }

    private lateinit var binding: FragmentEditViewBinding
    private var subjectIndex: Int? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditViewBinding.inflate(inflater, container, false)

        activity?.title = getString(R.string.edit)

        subjectIndex = arguments?.getInt("argSubjectIndex")!!

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getSubjectsAndObserveThem()
        getStateAndObserveIt()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save -> {
                val title = binding.titleEditText.text.toString()
                val description = binding.descriptionEditText.text.toString()
                editViewViewModel.updateSubject(subjectIndex!!, title, description)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSubjectsAndObserveThem() {
        editViewViewModel.getSubjects().observe(viewLifecycleOwner,
            Observer { subjects ->
                if (subjects != null && subjectIndex != -1) {
                    binding.titleEditText.setText(subjects[subjectIndex!!].title)
                    binding.descriptionEditText.setText(subjects[subjectIndex!!].description)
                }
                hideProgressBar()
            })
    }

    private fun getStateAndObserveIt() {
        var wasUpdatedBefore = false
        editViewViewModel.isUpdatingDb.observe(viewLifecycleOwner,
            Observer { updatingDb ->
                if (!updatingDb && wasUpdatedBefore) {
                    weekViewViewModel.reloadSubjects()
                    Navigation.findNavController(view!!).popBackStack()
                } else if (updatingDb) {
                    wasUpdatedBefore = true
                }
            })
    }

    private fun hideProgressBar() {
        binding.editViewProgressBar.visibility = View.GONE
        binding.editTitleLabel.visibility = View.VISIBLE
        binding.titleEditText.visibility = View.VISIBLE
        binding.editDescriptionLabel.visibility = View.VISIBLE
        binding.descriptionEditText.visibility = View.VISIBLE
    }
}
