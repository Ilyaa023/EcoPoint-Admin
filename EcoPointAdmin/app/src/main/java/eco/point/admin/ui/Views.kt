package eco.point.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eco.point.admin.R
import eco.point.admin.domain.models.Point
import eco.point.admin.domain.models.User
import java.util.*

//_CustomViews_
@Composable
fun MenuItem(menuState: Int, currState: Boolean){
    Box(contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .clickable { MainActivity.viewModel.setMenuState(menuState) }
            .background(if (currState) Color.White else colorResource(id = R.color.green))
    ) {
        Row(modifier = Modifier
            .padding(5.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = when (menuState) {
                    0 -> painterResource(id = R.drawable.ic_nav_add_location)
                    1 -> painterResource(id = R.drawable.ic_nav_users)
                    2 -> painterResource(id = R.drawable.ic_nav_bug)
                    3 -> painterResource(id = R.drawable.ic_nav_questions)
                    4 -> painterResource(id = R.drawable.ic_nav_add_location_alt)
                    5 -> painterResource(id = R.drawable.ic_nav_wrong_location)
                    else -> painterResource(id = R.drawable.ic_launcher_foreground)
                },
                contentDescription = "icon",
                tint = if (currState) colorResource(id = R.color.dark_green) else Color.White
            )
            Text(
                modifier = Modifier.padding(5.dp), text = when (menuState) {
                    0 -> stringResource(id = R.string.menu_points)
                    1 -> stringResource(id = R.string.menu_users)
                    2 -> stringResource(id = R.string.menu_bugs)
                    3 -> stringResource(id = R.string.menu_users_questions)
                    4 -> stringResource(id = R.string.menu_new_rso)
                    5 -> stringResource(id = R.string.menu_wrong_rso)
                    else -> ""
                }, color = if (currState) colorResource(id = R.color.dark_green) else Color.White
            )
        }
    }
}
@Composable
fun TextColumn(text: String, property: String){
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = text)
        Text(text = property)
    }
}

@Composable
fun RadioButtonItem(state: Boolean, text: String, callback: () -> Unit){
    Row(Modifier.clickable { callback() }) {
        RadioButton(
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.dark_green),
                unselectedColor = colorResource(id = R.color.dark_green)),
            selected = state,
            onClick = {  })
        Text(text = text, color = colorResource(id = R.color.dark_green), modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun CheckBoxItem(text: String, checked: Boolean, callback: (Boolean) -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { callback(!checked) }) {
        Checkbox(checked = checked, onCheckedChange = { callback(!checked) },
                 colors = CheckboxDefaults.colors(
                     checkedColor = colorResource(id = R.color.green),
                     uncheckedColor = colorResource(id = R.color.green)
                 ))
        Text(text = text, textAlign = TextAlign.Center, color = colorResource(id = R.color.dark_green))
    }
}

@Composable
fun TextFieldItem(text: String, fieldText: String, keyboardType: KeyboardType, callback: (String) -> Unit){
    Column {
        Divider()
        Text(text = text, color = colorResource(id = R.color.dark_green))
        TextField(
            value = fieldText,
            onValueChange = callback,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.textFieldColors(textColor = colorResource(id = R.color.dark_green))
        )
    }
}