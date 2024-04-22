package eco.point.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eco.point.admin.R
import eco.point.admin.domain.models.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//_Sheets_
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PointSheet(coroutineScope: CoroutineScope, bottomSheetState: ModalBottomSheetState){
    val sheetMode by MainActivity.viewModel.pointCreateSheetMode.observeAsState()
    val pointData by MainActivity.viewModel.pointData.observeAsState()
    val pointLatitude by MainActivity.viewModel.pointLatitude.observeAsState()
    val pointLongitude by MainActivity.viewModel.pointLongitude.observeAsState()
    LazyColumn(modifier = Modifier.background(Color.White)){
        item {
            Button(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = colorResource(id = R.color.green), contentColor = Color.White),
                onClick = {
                    if (sheetMode!!)
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    else {
                        MainActivity.viewModel.deletePoint()
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }
                }
            ){
                Text(text = if (sheetMode!!) "Отмена" else "Удалить", textAlign = TextAlign.Center)
            }
            Divider()
            Text(text = "Что это?")
            RadioButtonItem(state = pointData!!.size == Point.BIG, text = "Пункт с людьми"){MainActivity.viewModel.setPointProperty(size = Point.BIG)}
            RadioButtonItem(state = pointData!!.size == Point.SMALL, text = "Контейнер"){MainActivity.viewModel.setPointProperty(size = Point.SMALL)}
            TextFieldItem(text = "Адрес", fieldText = pointData!!.address, keyboardType = KeyboardType.Text,
                          callback = {MainActivity.viewModel.setPointProperty(address = it)})
            TextFieldItem(text = "Описание", fieldText = pointData!!.description, keyboardType = KeyboardType.Text,
                          callback = {MainActivity.viewModel.setPointProperty(description = it)})
            TextFieldItem(text = "Широта", fieldText = pointLatitude!!, keyboardType = KeyboardType.Number,
                          callback = {MainActivity.viewModel.setPointProperty(latitude = it)})
            TextFieldItem(text = "Долгота", fieldText = pointLongitude!!, keyboardType = KeyboardType.Number,
                          callback = {MainActivity.viewModel.setPointProperty(longitude = it)})
            TextFieldItem(text = "Организация", fieldText = pointData!!.organization, keyboardType = KeyboardType.Text,
                          callback = {MainActivity.viewModel.setPointProperty(organization = it)})
            Text(text = "Что принимают?")
            CheckBoxItem(checked = pointData!!.garbageTypes.contains(Point.PLASTIC), text = "Пластик") {
                MainActivity.viewModel.setPointProperty(
                    garbageTypes = MainActivity.viewModel.setPointTag(
                        tag = pointData!!.garbageTypes,
                        element = Point.PLASTIC,
                        checked = it
                    )
                )
            }
            CheckBoxItem(checked = pointData!!.garbageTypes.contains(Point.GLASS), text = "Стекло") {
                MainActivity.viewModel.setPointProperty(
                    garbageTypes = MainActivity.viewModel.setPointTag(
                        tag = pointData!!.garbageTypes,
                        element = Point.GLASS,
                        checked = it
                    )
                )
            }
            CheckBoxItem(checked = pointData!!.garbageTypes.contains(Point.METAL), text = "Металл") {
                MainActivity.viewModel.setPointProperty(
                    garbageTypes = MainActivity.viewModel.setPointTag(
                        tag = pointData!!.garbageTypes,
                        element = Point.METAL,
                        checked = it
                    )
                )
            }
            CheckBoxItem(checked = pointData!!.garbageTypes.contains(Point.PAPER), text = "Бумага") {
                MainActivity.viewModel.setPointProperty(
                    garbageTypes = MainActivity.viewModel.setPointTag(
                        tag = pointData!!.garbageTypes,
                        element = Point.PAPER,
                        checked = it
                    )
                )
            }
            CheckBoxItem(checked = pointData!!.garbageTypes.contains(Point.FOOD), text = "Пищевые отходы") {
                MainActivity.viewModel.setPointProperty(
                    garbageTypes = MainActivity.viewModel.setPointTag(
                        tag = pointData!!.garbageTypes,
                        element = Point.FOOD,
                        checked = it
                    )
                )
            }
            CheckBoxItem(checked = pointData!!.garbageTypes.contains(Point.BATTERY), text = "Батарейки, аккумуляторы и т.д."){
                MainActivity.viewModel.setPointProperty(
                    garbageTypes = MainActivity.viewModel.setPointTag(
                        tag = pointData!!.garbageTypes,
                        element = Point.BATTERY,
                        checked = it
                    )
                )
            }
            if (pointData!!.size == Point.BIG) {
                TextFieldItem(text = "Во сколько начинает работать?",
                              fieldText = pointData!!.startTime,
                              keyboardType = KeyboardType.Number,
                              callback = { MainActivity.viewModel.setPointProperty(startTime = it) })
                TextFieldItem(text = "Во сколько заканчивает работать?",
                              fieldText = pointData!!.endTime,
                              keyboardType = KeyboardType.Number,
                              callback = { MainActivity.viewModel.setPointProperty(endTime = it) })
            }
            Button(onClick = { MainActivity.viewModel.sendPoint() },
                   colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                   modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                Text(text = "OK", textAlign = TextAlign.Center)
            }
        }
    }
}


//_Fragments_
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PointsView(){
    MainActivity.viewModel.getPoints()
    val points = MainActivity.viewModel.pointsList.observeAsState()
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = Color.White,
        sheetContent = { PointSheet(coroutineScope, bottomSheetState) }
    ){
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = colorResource(id = R.color.green),
                    contentColor = Color.White,
                    onClick = {
                        MainActivity.viewModel.pointData.value = Point()
                        MainActivity.viewModel.setPointSheetMode(true)
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    }) {
                    Icon(painter = painterResource(id = android.R.drawable.ic_input_add), contentDescription = "")
                }
            }
        ) {
            LazyColumn(modifier = Modifier.padding(0.dp, 20.dp)){
                item {
                    for (point in points.value!!){
                        PointItem(point = point){
                            MainActivity.viewModel.setPointSheetMode(false)
                            MainActivity.viewModel.setPointProperty(
                                id = point.id,
                                address = point.address,
                                size = point.size,
                                description = point.description,
                                organization = point.organization,
                                garbageTypes = point.garbageTypes,
                                latitude = point.latitude.toString(),
                                longitude = point.longitude.toString(),
                                startTime = point.startTime,
                                endTime = point.endTime
                            )
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PointItem(point: Point, callback: () -> Unit){
    Card(modifier = Modifier
        .padding(5.dp)
        .clickable { callback() }, backgroundColor = Color.White) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(painter = painterResource(
                id = when (point.size) {
                    Point.BIG -> R.drawable.ic_point_big
                    Point.SMALL -> R.drawable.ic_point_small
                    else -> R.drawable.ic_nav_bug
                }
            ),
                 contentDescription = "icon", tint = colorResource(id = R.color.dark_green),
                 modifier = Modifier.align(Alignment.CenterVertically)
            )
            Column {
                Row {
                    Text(text = point.address, color = colorResource(id = R.color.dark_green))
                    Text(
                        text = point.id,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        color = colorResource(id = R.color.dark_green)
                    )
                }
                Text(text = point.organization, color = colorResource(id = R.color.dark_green))
            }
        }
    }
}

