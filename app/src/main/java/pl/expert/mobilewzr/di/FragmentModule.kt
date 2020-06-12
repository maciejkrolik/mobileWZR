package pl.expert.mobilewzr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.expert.mobilewzr.ui.lecturers.LecturersFragment
import pl.expert.mobilewzr.ui.lecturers.lecturerlogin.LecturersLoginFragment
import pl.expert.mobilewzr.ui.lecturers.lecturerstimetable.LecturersTimetableContainerFragment
import pl.expert.mobilewzr.ui.lecturers.lecturerstimetable.LecturersTimetableContentFragment
import pl.expert.mobilewzr.ui.news.NewsFragment
import pl.expert.mobilewzr.ui.news.notifications.NotificationsFragment
import pl.expert.mobilewzr.ui.search.SearchFragment
import pl.expert.mobilewzr.ui.timetable.TimetableContainerFragment
import pl.expert.mobilewzr.ui.timetable.calendarview.CalendarViewContentFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewContainerFragment
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewContentFragment
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

    @ContributesAndroidInjector
    abstract fun contributeLecturersTimetableContainerFragmentInjector(): LecturersTimetableContainerFragment

    @ContributesAndroidInjector
    abstract fun contributeLecturersTimetableContentFragmentInjector(): LecturersTimetableContentFragment

    @ContributesAndroidInjector
    abstract fun contributeLecturersFragmentInjector(): LecturersFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationsFragmentInjector(): NotificationsFragment

    @ContributesAndroidInjector
    abstract fun contributeLecturersLoginFragmentInjector(): LecturersLoginFragment

}