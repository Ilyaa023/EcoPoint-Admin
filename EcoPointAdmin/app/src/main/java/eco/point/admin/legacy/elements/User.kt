package eco.point.admin.legacy.elements

import com.google.firebase.database.DataSnapshot
import eco.point.admin.legacy.enums.UserData
import java.lang.Exception
import java.util.*

class User(dataSnapshot: DataSnapshot?) {
    lateinit var id: String
    private lateinit var name: String
    private lateinit var city: String
    private lateinit var bonuses: String
    private lateinit var level: String
    private lateinit var rating: String
    private lateinit var garbageCounter: String
    private var birthDate = GregorianCalendar()

    init {
        try {
            id = dataSnapshot!!.key.toString()
            for (data: DataSnapshot in dataSnapshot!!.children){
                val str = data.value.toString()
                when(data.key){
                    UserData.NAME.data -> name = str
                    UserData.CITY.data -> city = str
                    UserData.BONUSES.data -> bonuses = str
                    UserData.LEVEL.data -> level = str
                    UserData.RATING.data -> rating = str
                    UserData.GARBAGE_COUNTER.data -> garbageCounter = str
                    UserData.BIRTH.data -> birthDate.timeInMillis = str.toLong()
                }
            }
        }catch (e: Exception){}
    }

    fun getName(): String = name
    fun getCity(): String = city
    fun getLevel(): String = level
    fun getRating(): String = rating
    fun getBonuses(): String = bonuses
    fun getBirthDate(): GregorianCalendar = birthDate
    fun getGarbageCounter(): String = garbageCounter

    fun setName(s: String) {
        name = s
    }
    fun setCity(s: String) {
        city = s
    }
    fun setLevel(s: String) {
        level = s
    }
    fun setRating(s: String) {
        rating = s
    }
    fun setBonuses(s: String) {
        bonuses = s
    }
    fun setBirthDate(c: GregorianCalendar) {
        birthDate = c
    }
    fun setGarbageCounter(s: String) {
        garbageCounter = s
    }
}