package eco.point.admin.data

import com.google.firebase.database.FirebaseDatabase
import eco.point.admin.domain.models.TelegramMessage

class FBTelegramData {
    companion object{
        const val BUGS = "user_alarma"
        const val WRONG_POINTS = "RSO_point_alarma"
        const val USERS_QUESTIONS = "questions"
        const val NEW_POINTS = "new_RSO_point"
    }
    private val tg = FirebaseDatabase.getInstance().getReference("telegram")

    fun getMessage(type: String, callback:(ArrayList<TelegramMessage>) -> Unit){
        tg.child(type).get().addOnCompleteListener {
            if(it.isSuccessful){
                val list = ArrayList<TelegramMessage>()
                for (snap in it.result.children)
                    list.add(TelegramMessage(
                        id = snap.key.toString(),
                        message = snap.value.toString()
                    ))
                callback(list)
            }
        }
    }

    fun deleteMessage(type: String, telegramMessage: TelegramMessage){
        tg.child(type).child(telegramMessage.id).removeValue()
    }
}