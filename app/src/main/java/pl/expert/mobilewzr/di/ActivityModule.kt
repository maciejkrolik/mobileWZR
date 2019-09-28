package pl.expert.mobilewzr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.expert.mobilewzr.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity

}