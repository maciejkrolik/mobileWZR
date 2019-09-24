package pl.expert.mobilewzr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewContentFragment
import pl.expert.mobilewzr.ui.news.NewsFragment
import pl.expert.mobilewzr.ui.search.SearchFragment
import pl.expert.mobilewzr.ui.timetable.TimetableContainerFragment
import pl.expert.mobilewzr.ui.timetable.calendarview.CalendarViewContentFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewContainerFragment
import pl.expert.mobilewzr.ui.timetable.editview.EditViewFragment
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewContentFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeWeekViewContentFragmentInjector(): WeekViewContentFragment

    @ContributesAndroidInjector
    abstract fun contributeTimetableViewsContainerFragmentInjector(): TimetableContainerFragment

    @ContributesAndroidInjector
    abstract fun contributeNewsViewFragmentInjector(): NewsFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchViewFragmentInjector(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeDayViewContainerFragmentInjector(): DayViewContainerFragment

    @ContributesAndroidInjector
    abstract fun contributeDayViewContentFragmentInjector(): DayViewContentFragment

    @ContributesAndroidInjector
    abstract fun contributeCalendarViewFragmentInjector(): CalendarViewContentFragment

    @ContributesAndroidInjector
    abstract fun contributeEditViewFragmentInjector(): EditViewFragment

}