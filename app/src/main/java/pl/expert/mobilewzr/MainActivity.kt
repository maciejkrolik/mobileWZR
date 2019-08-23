package pl.expert.mobilewzr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupBottomNavMenu(navController)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
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
                R.id.others_view_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottom_nav_view.setupWithNavController(navController)
    }

}
