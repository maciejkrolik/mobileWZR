package pl.expert.mobilewzr.ui.testview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pl.expert.mobilewzr.R

// Created only for testing purposes
class TestViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.my_timetable)
        return inflater.inflate(R.layout.fragment_test_view, container, false)
    }
}
