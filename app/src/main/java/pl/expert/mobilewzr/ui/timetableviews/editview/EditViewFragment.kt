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

        activity?.title = ""

        subjectIndex = arguments?.getInt("argSubjectIndex") ?: -1

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
            R.id.delete -> {
                if (subjectIndex != -1)
                    editViewViewModel.deleteSubject(subjectIndex!!)
                else
                    Navigation.findNavController(view!!).popBackStack()
                return true
            }
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
        editViewViewModel.getSubjects(weekViewViewModel.groupId).observe(viewLifecycleOwner,
            Observer { subjects ->
                if (subjects != null && subjectIndex != -1) {
                    val subject = subjects.single { subject -> subject.index == subjectIndex }
                    binding.titleEditText.setText(subject.title)
                    binding.descriptionEditText.setText(subject.description)
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
        binding.chooseTimeLabel.visibility = View.VISIBLE
        binding.chooseTimeSpinner.visibility = View.VISIBLE
    }
}
