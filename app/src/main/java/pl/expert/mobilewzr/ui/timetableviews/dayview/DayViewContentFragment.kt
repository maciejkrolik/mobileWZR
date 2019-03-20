package pl.expert.mobilewzr.ui.timetableviews.dayview

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.data.dto.DayViewItem
import pl.expert.mobilewzr.databinding.FragmentDayViewContentBinding
import javax.inject.Inject

class DayViewContentFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDayViewContentBinding
    private var weekNumber: Int? = null
    private var weekDay: Int? = null

    private val dayViewItems: MutableList<DayViewItem> = mutableListOf()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDayViewContentBinding.inflate(inflater, container, false)

        val recyclerAdapter = DayViewRecyclerAdapter(dayViewItems)

        binding.dayViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dayViewViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory).get(DayViewViewModel::class.java)

        weekNumber = arguments?.getInt("argWeekNumber")!!
        weekDay = arguments?.getInt("argWeekDayNumber")!!

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
}
