package eco.point.admin.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import eco.point.admin.R
import eco.point.admin.data.FBTelegramData
import eco.point.admin.domain.models.TelegramMessage
import java.lang.Exception

@Composable
fun BugsView(){
    MainActivity.viewModel.getBugs()
    val bugs by MainActivity.viewModel.bugsList.observeAsState()
    LazyColumn{
        item {
            for (bug in bugs!!)
                TelegramItem(FBTelegramData.BUGS, bug)
        }
    }
}

@Composable
fun WrongPointView(){
    MainActivity.viewModel.getWrongPoints()
    val points by MainActivity.viewModel.wrongPointsList.observeAsState()
    LazyColumn{
        item {
            for (point in points!!)
                TelegramItem(type = FBTelegramData.WRONG_POINTS, telegramMessage = point)
        }
    }
}

@Composable
fun NewPointView(){
    MainActivity.viewModel.getNewPoints()
    val points by MainActivity.viewModel.newPointsList.observeAsState()
    LazyColumn{
        item {
            for (point in points!!)
                TelegramItem(type = FBTelegramData.NEW_POINTS, telegramMessage = point)
        }
    }
}

@Composable
fun QuestionsView(){
    MainActivity.viewModel.getQuestions()
    val points by MainActivity.viewModel.questionsList.observeAsState()
    LazyColumn{
        item {
            for (point in points!!)
                TelegramItem(type = FBTelegramData.USERS_QUESTIONS, telegramMessage = point)
        }
    }
}

@Composable
fun TelegramItem(type: String, telegramMessage: TelegramMessage){
    Card(modifier = Modifier.padding(5.dp)) {
        Column {
            Text(text = telegramMessage.id, Modifier.padding(5.dp))
            Text(text = telegramMessage.message, Modifier.padding(5.dp))
            Row(Modifier.fillMaxWidth()) {
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                       modifier = Modifier.padding(5.dp),
                       onClick = { MainActivity.viewModel.deleteTgMessage(type, telegramMessage) }) {
                    Text(text = "Удалить")
                }
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                       modifier = Modifier
                           .padding(5.dp)
                           .weight(1f),
                       onClick = {
                    try {
                        val telegram = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://t.me/${telegramMessage.getTgId()}"
                            ))
                        Log.i("startTlg: ", "startTlg: https://t.me/${telegramMessage.getTgId()}")
                        telegram.setPackage("org.telegram.messenger")
                        MainActivity.context.startActivity(telegram)
                    }catch (e: Exception){
                        e.printStackTrace()
                        Toast.makeText(MainActivity.context, "Telegram app is not installed", Toast.LENGTH_LONG).show();
                    }
                }) {
                    Text(text = "Открыть в Telegram")
                }
            }
        }
    }
}