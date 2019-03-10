package pl.expert.mobilewzr.ui.weekview

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.databinding.FragmentWeekViewContainerBinding
import pl.expert.mobilewzr.util.CalendarUtils
import javax.inject.Inject

class WeekViewContainerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentWeekViewContainerBinding
    private lateinit var groupId: String

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewContainerBinding.inflate(inflater, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        groupId = arguments?.getString("argGroupId") ?: sharedPref.getString("prefSavedGroupId", "")!!

        if (!groupId.isEmpty()) {
            activity?.title = getString(R.string.group) + ": $groupId"

            val pagerAdapter = WeekViewPagerAdapter(context, childFragmentManager)
            binding.weekViewViewPager.adapter = pagerAdapter
            binding.weekViewTabLayout.setupWithViewPager(binding.weekViewViewPager)
            binding.weekViewViewPager.currentItem = CalendarUtils.getWeekNumber()

            binding.weekViewViewPager.visibility = View.VISIBLE
            binding.weekViewTabLayout.visibility = View.VISIBLE
        } else {
            activity?.title = getString(R.string.my_timetable)
            binding.weekViewContainerTextInfo.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!groupId.isEmpty()) {
            val weekViewViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(WeekViewViewModel::class.java)

            weekViewViewModel.loadSubjectsFromRepository(groupId)
        }
    }
}
