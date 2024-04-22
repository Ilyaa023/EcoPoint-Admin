package eco.point.admin.domain.models

class TelegramMessage(
        val id: String = "defId",
        val message: String = "defMessage"
) {
    fun getTgId(): String {
        var str = ""
        var counter = 0
        while (id[counter] != '_' && counter < id.length){
            str += id[counter]
            counter++
        }
        return str
    }
}