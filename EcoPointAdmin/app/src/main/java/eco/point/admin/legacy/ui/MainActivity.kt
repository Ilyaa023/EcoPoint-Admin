package eco.point.admin.legacy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import eco.point.admin.legacy.ui.ui.theme.EcoPointAdminTheme
import eco.point.admin.R
import eco.point.admin.legacy.elements.Point
import eco.point.admin.legacy.firebase.auth.FBConnectAuth
import eco.point.admin.ui.MainActivityViewModel

class MainActivity : ComponentActivity() {
    companion object{
        lateinit var viewModel: MainActivityViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContent {
            EcoPointAdminTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val fbConnect = FBConnectAuth(this)
        fbConnect.GoogleSignOut()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Greeting() {
//    val menuState by MainActivity.viewModel.menuState.observeAsState()
//    BackdropScaffold(
//        backLayerBackgroundColor = colorResource(id = R.color.green),
//        appBar = { TopAppBar(
//            navigationIcon = { Icon(
//                painter = when (menuState){
//                    0 -> painterResource(id = R.drawable.ic_nav_add_location)
//                    1 -> painterResource(id = R.drawable.ic_nav_users)
//                    2 -> painterResource(id = R.drawable.ic_nav_bug)
//                    3 -> painterResource(id = R.drawable.ic_nav_questions)
//                    4 -> painterResource(id = R.drawable.ic_nav_add_location_alt)
//                    5 -> painterResource(id = R.drawable.ic_nav_wrong_location)
//                    else -> painterResource(id = R.drawable.ic_launcher_foreground)
//                                          },
//                contentDescription = "icon",
//                tint = Color.White
//            )},
//            contentColor = Color.White,
//            backgroundColor = colorResource(id = R.color.green),
//            title = { Text(text = when (menuState){
//                0 -> stringResource(id = R.string.menu_points)
//                1 -> stringResource(id = R.string.menu_users)
//                2 -> stringResource(id = R.string.menu_bugs)
//                3 -> stringResource(id = R.string.menu_users_questions)
//                4 -> stringResource(id = R.string.menu_new_rso)
//                5 -> stringResource(id = R.string.menu_wrong_rso)
//                else -> ""
//            }) }
//        ) },
//        backLayerContent = { BackLayer(menuState!!) },
//        frontLayerContent = { FrontLayer(menuState!!) })
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EcoPointAdminTheme {
        Greeting()
    }
}