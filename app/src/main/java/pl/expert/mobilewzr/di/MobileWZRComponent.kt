package pl.expert.mobilewzr.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import pl.expert.mobilewzr.MobileWZRApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class, ViewModelModule::class, ActivityModule::class,
        AndroidSupportInjectionModule::class]
)
interface MobileWZRComponent {

    fun inject(application: MobileWZRApplication)
}