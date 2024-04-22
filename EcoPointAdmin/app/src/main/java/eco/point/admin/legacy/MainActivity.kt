package eco.point.admin.legacy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eco.point.admin.legacy.firebase.auth.FBConnectAuth

class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_points, R.id.nav_users, R.id.nav_bugs, R.id.nav_questions, R.id.nav_new_point, R.id.nav_wrong_point, R.id.nav_exchange //R.id.nav_articles
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        val fbConnect = FBConnectAuth(this)
        fbConnect.GoogleSignOut()
    }
}