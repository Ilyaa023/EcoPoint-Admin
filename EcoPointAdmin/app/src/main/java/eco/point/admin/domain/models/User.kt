package eco.point.admin.domain.models

import java.util.*

class User(
        val id: String = "defId",
        val name: String = "defName",
        val city: String = "defCity",
        val bonuses: Int = 0,
        val level: Int = 0,
        val rating: Int = 0,
        val garbageCounter: Int = 0,
        val birthDate: GregorianCalendar = GregorianCalendar(),
        val userStatus: Int = 0,
) {}