package eco.point.admin.legacy.elements

import com.google.firebase.database.DataSnapshot

class Telegram(dataSnapshot: DataSnapshot) {
    private var id: String
    private var message: String

    init {
        id = dataSnapshot.key.toString()
        message = dataSnapshot.value.toString()
    }

    fun getID(): String = id
    fun getMessage(): String = message
    fun getTgID(): String {
        var str = ""
        var counter = 0
        while (id[counter] != '_' && counter < id.length){
            str += id[counter]
            counter++
        }
        return str
    }

    fun setID(s: String){id = s}
    fun setMessage(s: String){message = s}

}