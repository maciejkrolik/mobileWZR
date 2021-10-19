package pl.expert.mobilewzr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupBottomNavMenu(navController)
    }

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.my_timetable_view_fragment,
                R.id.search_view_fragment,
                R.id.news_view_fragment,
                R.id.lecturers_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottomNavMenu.setupWithNavController(navController)
        bottomNavMenu.setOnNavigationItemReselectedListener { menuItem ->
            if (menuItem.itemId != navController.currentDestination?.id) {
                while (navController.currentDestination?.id != menuItem.itemId) {
                    navController.popBackStack()
                }
            }
        }
    }

}
