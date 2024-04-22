package eco.point.admin

import android.util.Log
import eco.point.admin.domain.models.Point
import eco.point.admin.ui.PointsView
import eco.point.admin.ui.setTag
import org.junit.Test

class TagTest() {
    @Test
    fun testTag(){
        var tag = Point.PLASTIC + Point.GLASS + Point.BATTERY
        println("testTag: ${setTag(tag = tag, element = Point.FOOD, checked = true)}")
        println("testTag: ${setTag(tag = tag, element = Point.FOOD, checked = false)}")
        println("testTag: ${setTag(tag = tag, element = Point.PLASTIC, checked = true)}")
        println("testTag: ${setTag(tag = tag, element = Point.PLASTIC, checked = false)}")
    }
}