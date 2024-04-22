package eco.point.admin.legacy.elements

import java.util.*

class Complaint(
        val cId: String = "defCId_${GregorianCalendar().timeInMillis}",
//        val cUser: User = User(),
        val announcement: Announcement = Announcement(),
        val confirm: Boolean = false,
        var text: String = ""
) {}