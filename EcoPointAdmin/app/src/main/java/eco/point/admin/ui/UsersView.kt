package eco.point.admin.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eco.point.admin.R
import eco.point.admin.domain.models.User
import java.util.*

@Composable
fun UsersView(){
    MainActivity.viewModel.getUsers()
    val users by MainActivity.viewModel.usersList.observeAsState()
    Log.e("TAG", "UsersView: $users", )
    LazyColumn{
        item {
            for (user in users!!){
                UserItem(user = user)
            }
        }
    }
}

@Composable
fun UserItem(user: User){
    Card(modifier = Modifier.padding(5.dp)) {
        Row {
            Icon(painter = painterResource(id = R.drawable.ic_nav_users), contentDescription = "ic",
                 modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(text = user.id, modifier = Modifier.padding(5.dp))
                Text(text = "${user.name}, ${user.city}, ${when(user.userStatus){
                    0 -> "Новичок"
                    1 -> "Продвинутый"
                    2 -> "Профессионал"
                    else -> "Инопришеленец"
                }}", )
                Text(modifier = Modifier.padding(5.dp), text = "${user.birthDate[GregorianCalendar.DAY_OF_MONTH]}." +
                        "${user.birthDate[GregorianCalendar.MONTH] + 1}." +
                        "${user.birthDate[GregorianCalendar.YEAR]}")
                Row(Modifier.fillMaxWidth()){
                    TextColumn(text = "Количество сдач", property = user.garbageCounter.toString())
                    TextColumn(text = "Количество бонусов", property = user.bonuses.toString())
                }
                Row(Modifier.fillMaxWidth()) {
                    TextColumn(text = "Уровень", property = user.level.toString())
                    TextColumn(text = "Рейтинг", property = user.rating.toString())
                }

            }
        }
    }
}