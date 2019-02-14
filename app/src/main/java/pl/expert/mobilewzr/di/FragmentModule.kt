package pl.expert.mobilewzr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.expert.mobilewzr.ui.weekview.WeekViewFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeWeekViewFragmentInjector(): WeekViewFragment
}