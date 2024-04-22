package eco.point.admin.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import eco.point.admin.R
import eco.point.admin.data.FBAuth
import eco.point.admin.ui.ui.theme.EcoPointAdminTheme

class MainActivity : ComponentActivity() {
    companion object{
        lateinit var viewModel: MainActivityViewModel
        lateinit var context: Context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setContent {
            EcoPointAdminTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FBAuth(this).googleSignOut()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView() {
    val menuState by MainActivity.viewModel.menuState.observeAsState()
    BackdropScaffold(
        backLayerBackgroundColor = colorResource(id = R.color.green),
        frontLayerBackgroundColor = Color.White,
        appBar = { TopAppBar(
            navigationIcon = { Icon(
                painter = when (menuState){
                    0 -> painterResource(id = R.drawable.ic_nav_add_location)
                    1 -> painterResource(id = R.drawable.ic_nav_users)
                    2 -> painterResource(id = R.drawable.ic_nav_bug)
                    3 -> painterResource(id = R.drawable.ic_nav_questions)
                    4 -> painterResource(id = R.drawable.ic_nav_add_location_alt)
                    5 -> painterResource(id = R.drawable.ic_nav_wrong_location)
                    else -> painterResource(id = R.drawable.ic_launcher_foreground)
                },
                contentDescription = "icon",
                tint = Color.White
            )
            },
            contentColor = Color.White,
            backgroundColor = colorResource(id = R.color.green),
            title = { Text(text = when (menuState){
                0 -> stringResource(id = R.string.menu_points)
                1 -> stringResource(id = R.string.menu_users)
                2 -> stringResource(id = R.string.menu_bugs)
                3 -> stringResource(id = R.string.menu_users_questions)
                4 -> stringResource(id = R.string.menu_new_rso)
                5 -> stringResource(id = R.string.menu_wrong_rso)
                else -> ""
            }) }
        ) },
        backLayerContent = { BackLayer(menuState!!) },
        frontLayerContent = { FrontLayer(menuState!!) })
}

//_Layers_
@Composable
fun BackLayer(menuState: Int){
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            MenuItem(menuState = 0, menuState == 0)
            MenuItem(menuState = 1, menuState == 1)
            MenuItem(menuState = 2, menuState == 2)
            MenuItem(menuState = 3, menuState == 3)
            MenuItem(menuState = 4, menuState == 4)
            MenuItem(menuState = 5, menuState == 5)
        }
    }
}

@Composable
fun FrontLayer(menuState: Int){
    when (menuState){
        0 -> PointsView()
        1 -> UsersView()
        2 -> BugsView()
        3 -> QuestionsView()
        4 -> NewPointView()
        5 -> WrongPointView()
    }
}
//_Preview_
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    EcoPointAdminTheme {
//        MainView()
//        PointItem(point = Point())
//        PointSheet()
    }
}