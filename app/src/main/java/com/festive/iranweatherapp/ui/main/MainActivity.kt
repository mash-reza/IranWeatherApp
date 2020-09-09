package com.festive.iranweatherapp.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.farsitel.bazaar.IUpdateCheckService
import com.festive.iranweatherapp.R
import com.festive.iranweatherapp.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var mainViewModel: MainViewModel

    private var requestedFinish: Boolean = false
    var service: IUpdateCheckService? = null
    var connection: UpdateServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initService()
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        mainViewModel.setMainState()

        setSupportActionBar(toolbar)

//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)
        setupNavigation()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            requestedFinish -> {
                finish()
            }
            else -> {
                requestedFinish = true
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    requestedFinish = false
                }
                Toast.makeText(this, "برای خروج مجددا دکمه بازگشت را بفشارید", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_choose
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.apply {
            setupWithNavController(navController)
        }
        mainViewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]
        mainViewModel.observeMainState().observe(this, Observer {
            when (it) {
                is MainState.Home -> {
                    navView.menu.findItem(R.id.navViewChooseMenuItem).isVisible = true
                }
                MainState.Choose -> {
                    navView.menu.findItem(R.id.navViewChooseMenuItem).isVisible = false
                }
            }
        })
        navView.bringToFront()
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navViewChooseMenuItem -> {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    mainViewModel.setMainState(MainState.Choose)
                }
                R.id.navViewStoreCommentMenuItem -> commentOnBazaar()

                R.id.navViewStoreMenuItem -> reviewMyBazaarPage()
                else -> return@setNavigationItemSelectedListener false
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun commentOnBazaar() {
        if (isBazaarInstalled()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("bazaar://details?id=com.festive.iranweatherapp")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        } else Toast.makeText(
            this,
            "برنامه کافه بازار بر روی دستگاه شما نصب نشده است!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun reviewMyBazaarPage() {
        if (isBazaarInstalled()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("bazaar://collection?slug=by_author&aid=reznov")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        } else Toast.makeText(
            this,
            "برنامه کافه بازار بر روی دستگاه شما نصب نشده است!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseService()
    }


    inner class UpdateServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, boundService: IBinder) {
            service = IUpdateCheckService.Stub
                .asInterface(boundService as IBinder)
            try {
                val newVersion = service?.getVersionCode("com.festive.iranweatherapp")
                val pInfo: PackageInfo =
                    applicationContext.packageManager.getPackageInfo(packageName, 0)
                val oldVersion = pInfo.versionCode
                if (((newVersion ?: -1) > oldVersion) && isBazaarInstalled()) {
                    UpdateDialogFragment().apply {
                        onUpdateFragmentActionListener =
                            object : UpdateDialogFragment.OnUpdateFragmentActionListener {
                                override fun agree() {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse("bazaar://details?id=com.festive.iranweatherapp")
                                    intent.setPackage("com.farsitel.bazaar")
                                    startActivity(intent)
                                    dismiss()
                                }

                                override fun disAgree() {
                                    dismiss()
                                }
                            }
                        show(supportFragmentManager, "updateFragment")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
            Log.e(TAG, "onServiceConnected(): Connected");
        }

        override fun onServiceDisconnected(name: ComponentName) {
            service = null
            Log.e(TAG, "onServiceDisconnected(): Disconnected");
        }
    }

    private fun initService() {
        Log.i(TAG, "initService()")
        connection = UpdateServiceConnection()
        val i = Intent(
            "com.farsitel.bazaar.service.UpdateCheckService.BIND"
        )
        i.setPackage("com.farsitel.bazaar")
        val ret = bindService(i, connection, Context.BIND_AUTO_CREATE)
        Log.e(TAG, "initService() bound value: $ret")
    }

    private fun releaseService() {
        unbindService(connection)
        connection = null
        Log.d(TAG, "releaseService(): unbound.")
    }

    private fun isBazaarInstalled(): Boolean =
        try {
            packageManager.getPackageInfo("com.farsitel.bazaar", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
}
