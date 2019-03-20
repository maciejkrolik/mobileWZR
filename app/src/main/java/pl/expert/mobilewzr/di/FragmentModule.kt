package pl.expert.mobilewzr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.expert.mobilewzr.ui.dayview.DayViewContentFragment
import pl.expert.mobilewzr.ui.newsview.NewsViewFragment
import pl.expert.mobilewzr.ui.searchview.SearchViewFragment
import pl.expert.mobilewzr.ui.weekview.WeekViewContainerFragment
import pl.expert.mobilewzr.ui.weekview.WeekViewContentFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeWeekViewContentFragmentInjector(): WeekViewContentFragment

    @ContributesAndroidInjector
    abstract fun contributeWeekViewContainerFragmentInjector(): WeekViewContainerFragment

    @ContributesAndroidInjector
    abstract fun contributeNewsViewFragmentInjector(): NewsViewFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchViewFragmentInjector(): SearchViewFragment

    @ContributesAndroidInjector
    abstract fun contributeDayViewContentFragmentInjector(): DayViewContentFragment
}