package eco.point.admin.domain.models

import java.util.*

class Point constructor(
        val id: String = GregorianCalendar().timeInMillis.toString(),
        val address: String = "",
        val size: Int = SMALL,
        val description: String = "",
        val organization: String = "",
        val garbageTypes: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val startTime: String = "9:00",
        val endTime: String = "21:00"
) {
    companion object{
        val BIG = 1
        val SMALL = 0
        val PLASTIC = "plastic "
        val GLASS = "glass "
        val METAL = "metal "
        val PAPER = "paper "
        val FOOD = "food "
        val BATTERY = "battery "
    }
}