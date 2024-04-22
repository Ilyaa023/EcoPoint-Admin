package eco.point.admin.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eco.point.admin.domain.models.User
import java.util.*
import kotlin.collections.ArrayList

class FBUsersData {
    companion object{
        const val NAME = "Name"
        const val CITY = "City"
        const val STATUS = "UserStatus"
        const val BONUSES = "Bonuses"
        const val LEVEL = "Level"
        const val RATING = "Rating"
        const val GARBAGE_COUNTER = "GarbageHandOverCount"
        const val BIRTH = "BirthDate"
        private const val ROLE = "role"
    }
    private val users = FirebaseDatabase.getInstance().getReference("Users")

    fun getUsers(callback: (ArrayList<User>) -> Unit){
        users.get().addOnCompleteListener {
            if(it.isSuccessful){
                val list = ArrayList<User>()
                for (snap in it.result.children){
                    try {
                        list.add(User(
                            id = snap.key.toString(),
                            name = snap.child(NAME).value.toString(),
                            city = snap.child(CITY).value.toString(),
                            userStatus = snap.child(STATUS).value.toString().toInt(),
                            bonuses = snap.child(BONUSES).value.toString().toInt(),
                            level = snap.child(LEVEL).value.toString().toInt(),
                            rating = snap.child(RATING).value.toString().toInt(),
                            garbageCounter = snap.child(GARBAGE_COUNTER).value.toString().toInt(),
                            birthDate = GregorianCalendar().apply {
                                timeInMillis = snap.child(BIRTH).value.toString().toLong()
                            }
                        ))
                    } catch (e: Exception){ e.printStackTrace() }
                }
                callback(list)
            }
        }
    }

    fun adminCheck(callback: (Boolean) -> Unit) {
        users.child(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnCompleteListener {
            callback (it.result.child(ROLE).value.toString() == "admin")
        }
    }
}