package pl.expert.mobilewzr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.expert.mobilewzr.ui.timetableviews.dayview.DayViewContentFragment
import pl.expert.mobilewzr.ui.newsview.NewsViewFragment
import pl.expert.mobilewzr.ui.searchview.SearchViewFragment
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewsContainerFragment
import pl.expert.mobilewzr.ui.timetableviews.editview.EditViewFragment
import pl.expert.mobilewzr.ui.timetableviews.weekview.WeekViewContentFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeWeekViewContentFragmentInjector(): WeekViewContentFragment

    @ContributesAndroidInjector
    abstract fun contributeTimetableViewsContainerFragmentInjector(): TimetableViewsContainerFragment

    @ContributesAndroidInjector
    abstract fun contributeNewsViewFragmentInjector(): NewsViewFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchViewFragmentInjector(): SearchViewFragment

    @ContributesAndroidInjector
    abstract fun contributeDayViewContentFragmentInjector(): DayViewContentFragment

    @ContributesAndroidInjector
    abstract fun contributeEditViewFragmentInjector(): EditViewFragment
}