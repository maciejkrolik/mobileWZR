package pl.expert.mobilewzr.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import pl.expert.mobilewzr.MobileWZRApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        AndroidInjectionModule::class]
)
interface MobileWZRComponent {

    fun inject(application: MobileWZRApplication)

}