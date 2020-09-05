package com.festive.iranweatherapp.ui.main

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.festive.iranweatherapp.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject lateinit var mainViewModel: MainViewModel

    private var requestedFinish:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        mainViewModel.setMainState()

        setSupportActionBar(toolbar)

//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//        return true
//    }

    override fun onBackPressed() {
//        finish()
        if(requestedFinish){
           finish()
        }else{
            requestedFinish = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                requestedFinish = false
            }
            Toast.makeText(this, "برای خروج مجددا دکمه بازگشت را بفشارید", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigation(){
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,R.id.chooseFragmentWeatherIMageView
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}