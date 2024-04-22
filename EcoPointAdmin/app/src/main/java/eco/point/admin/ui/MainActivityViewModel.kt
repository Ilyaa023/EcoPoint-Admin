package eco.point.admin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.admin.data.FBPointsData
import eco.point.admin.data.FBTelegramData
import eco.point.admin.data.FBUsersData
import eco.point.admin.domain.models.Point
import eco.point.admin.domain.models.TelegramMessage
import eco.point.admin.domain.models.User

class MainActivityViewModel: ViewModel() {
    val menuState = MutableLiveData(0)
    //lists
    val pointsList = MutableLiveData(ArrayList<Point>())
    val usersList = MutableLiveData(ArrayList<User>())
    val bugsList = MutableLiveData(ArrayList<TelegramMessage>())
    val wrongPointsList = MutableLiveData(ArrayList<TelegramMessage>())
    val questionsList = MutableLiveData(ArrayList<TelegramMessage>())
    val newPointsList = MutableLiveData(ArrayList<TelegramMessage>())
    //values for create and edit points
    val pointData = MutableLiveData(Point())
    val pointLatitude = MutableLiveData("")
    val pointLongitude = MutableLiveData("")
    val pointCreateSheetMode = MutableLiveData(true)

    //Menu
    fun setMenuState(state: Int){
        if (state >= 0 || state < 6)
            menuState.value = state
    }

    //Points
    fun getPoints(){
        FBPointsData().getPoints {
            pointsList.value = it
        }
    }
    fun setPointProperty(
            id: String = pointData.value!!.id,
            address: String = pointData.value!!.address,
            size: Int = pointData.value!!.size,
            description: String = pointData.value!!.description,
            organization: String = pointData.value!!.organization,
            garbageTypes: String = pointData.value!!.garbageTypes,
            latitude: String = pointLatitude.value!!,
            longitude: String = pointLongitude.value!!,
            startTime: String = pointData.value!!.startTime,
            endTime: String = pointData.value!!.endTime
    ){
        pointData.value = Point(id = id, address = address, size = size, description = description,
            organization = organization, garbageTypes = garbageTypes,
            latitude = if(latitude.matches(Regex("^(-)?([0-9]+)(.[0-9]+)?")))
                latitude.toDouble()
            else 0.0,
            longitude = if(longitude.matches(Regex("^(-)?([0-9]+)(.[0-9]+)?")))
                longitude.toDouble()
            else 0.0,
            startTime = startTime, endTime = endTime)
        pointLatitude.value = latitude
        pointLongitude.value = longitude
    }
    fun setPointSheetMode(mode: Boolean){
        pointCreateSheetMode.value = mode
    }
    fun setPointTag(tag: String, element: String, checked: Boolean): String {
        val types = arrayOf(Point.PLASTIC, Point.GLASS, Point.METAL,
                            Point.PAPER, Point.FOOD, Point.BATTERY)
        val garbageTypes = arrayOf(tag.contains(types[0]), tag.contains(types[1]),
                                   tag.contains(types[2]), tag.contains(types[3]),
                                   tag.contains(types[4]), tag.contains(types[5])
        )
        val numOfElement = types.indexOf(element)
        var tagStr = ""
        for (i in types.indices){
            if (i == numOfElement && checked || i != numOfElement && garbageTypes[i])
                tagStr += types[i]
        }
        return tagStr
    }
    fun sendPoint(){
        FBPointsData().setPoint(point = pointData.value!!)
    }
    fun deletePoint(){
        FBPointsData().deletePoint(point = pointData.value!!)
    }

    //Users
    fun getUsers(){
        FBUsersData().getUsers {
            usersList.value = it
        }
    }

    //Telegram
    fun getBugs(){
        FBTelegramData().getMessage(FBTelegramData.BUGS){ bugsList.value = it }
    }
    fun getWrongPoints(){
        FBTelegramData().getMessage(FBTelegramData.WRONG_POINTS){ wrongPointsList.value = it }
    }
    fun getNewPoints(){
        FBTelegramData().getMessage(FBTelegramData.NEW_POINTS){ newPointsList.value = it }
    }
    fun getQuestions(){
        FBTelegramData().getMessage(FBTelegramData.USERS_QUESTIONS){ questionsList.value = it }
    }
    fun deleteTgMessage(type: String, tgMessage: TelegramMessage){
        FBTelegramData().deleteMessage(type, tgMessage)
    }
}