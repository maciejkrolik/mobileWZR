package pl.expert.mobilewzr.weekview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.WZRService
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.model.WeekViewItem
import pl.expert.mobilewzr.databinding.FragmentWeekViewBinding
import pl.expert.mobilewzr.util.CalendarUtils
import pl.expert.mobilewzr.util.WeekViewUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeekViewFragment : Fragment() {

    @Inject
    lateinit var wzrService: WZRService

    private lateinit var binding: FragmentWeekViewBinding

    private val listOfSubjects: MutableList<Subject> = mutableListOf()
    private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: WeekViewAdapter
    private lateinit var viewManager: LinearLayoutManager

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeekViewBinding.inflate(inflater, container, false)

        viewManager = LinearLayoutManager(context)
        viewAdapter = WeekViewAdapter(listOfWeekViewItems)

        recyclerView = binding.weekViewRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        if (CalendarUtils.getWeekNumber() == 0) {
            activity?.title = getString(R.string.weekA)
        } else {
            activity?.title = getString(R.string.weekB)
        }

        wzrService.listSubjects("S22-31").enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                if (response.body() != null) {
                    listOfSubjects.addAll(response.body() as List<Subject>)

                    listOfWeekViewItems.addAll(WeekViewUtils.getEmptyListOfWeekViewItemsWithTimes())

                    Toast.makeText(context, "Pobrano dane", Toast.LENGTH_SHORT).show()

                    for (subject in listOfSubjects) {
                        if (CalendarUtils.getWeekNumber(subject.startDate) == 0) {
                            when (subject.startTime) {
                                "08.00" -> setSubjectTitle(0, subject)
                                "08.45" -> setSubjectTitle(1, subject)
                                "09.45" -> setSubjectTitle(2, subject)
                                "10.30" -> setSubjectTitle(3, subject)
                                "11.30" -> setSubjectTitle(4, subject)
                                "12.15" -> setSubjectTitle(5, subject)
                                "13.30" -> setSubjectTitle(6, subject)
                                "14.15" -> setSubjectTitle(7, subject)
                                "15.15" -> setSubjectTitle(8, subject)
                                "16.00" -> setSubjectTitle(9, subject)
                                "17.00" -> setSubjectTitle(10, subject)
                                "17.45" -> setSubjectTitle(11, subject)
                                "18.45" -> setSubjectTitle(12, subject)
                                "19.30" -> setSubjectTitle(13, subject)
                                "20.15" -> setSubjectTitle(14, subject)
                            }
                        } else {
                            break
                        }
                    }

                    binding.weekViewProgressBar.visibility = View.GONE
                    binding.weekViewRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                binding.weekViewProgressBar.visibility = View.GONE
                activity?.title = getString(R.string.retrofit_error)
                Toast.makeText(context, getString(R.string.retrofit_error), Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }
        })

        return binding.root
    }

    private fun setSubjectTitle(position: Int, subject: Subject) {
        listOfWeekViewItems[position].listOfSubjects[CalendarUtils.getDayOfWeek(subject.startDate)] = subject.title
    }
}
