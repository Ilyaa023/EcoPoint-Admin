package eco.point.admin.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eco.point.admin.domain.models.Point

class FBPointsData {
    companion object{
        val ADDRESS = "Address"
        val DESCRIPTION = "Description"
        val LATITUDE = "Latitude"
        val LONGITUDE = "Longitude"
        val ORGANIZATION = "Organization"
        val PHOTO = "Photo"
        val POINT_RATING = "Rating"
        val TAG = "TAG"
        val END_TIME = "EndTime"
        val START_TIME = "StartTime"
    }

    private val points = FirebaseDatabase.getInstance().getReference("Points")

    fun getPoints(callback: (ArrayList<Point>) -> Unit){
        points.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Point>()
                for (snap in snapshot.child("Big").children){
                    try {
                        list.add(
                            Point(
                                id = snap.key.toString(),
                                address = snap.child(ADDRESS).value.toString(),
                                size = Point.BIG,
                                description = snap.child(DESCRIPTION).value.toString(),
                                organization = snap.child(ORGANIZATION).value.toString(),
                                garbageTypes = snap.child(TAG).value.toString(),
                                latitude = snap.child(LATITUDE).value.toString().toDouble(),
                                longitude = snap.child(LONGITUDE).value.toString().toDouble(),
                                startTime = snap.child(START_TIME).value.toString(),
                                endTime = snap.child(END_TIME).value.toString()
                            )
                        )
                    } catch (e: Exception) {e.printStackTrace()}
                }
                for (snap in snapshot.child("Small").children){
                    try {
                        list.add(
                            Point(
                                id = snap.key.toString(),
                                address = snap.child(ADDRESS).value.toString(),
                                size = Point.SMALL,
                                description = snap.child(DESCRIPTION).value.toString(),
                                organization = snap.child(ORGANIZATION).value.toString(),
                                garbageTypes = snap.child(TAG).value.toString(),
                                latitude = snap.child(LATITUDE).value.toString().toDouble(),
                                longitude = snap.child(LONGITUDE).value.toString().toDouble(),
                            )
                        )
                    } catch (e: Exception) {e.printStackTrace()}
                }
                callback(list)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun setPoint(point: Point){
        val pointDB = points
            .child(when (point.size) {
                Point.BIG -> "Big"
                Point.SMALL -> "Small"
                else -> "ERROR"
            })
            .child(point.id)
        pointDB.child(ADDRESS).setValue(point.address)
        pointDB.child(DESCRIPTION).setValue(point.description)
        pointDB.child(ORGANIZATION).setValue(point.organization)
        pointDB.child(TAG).setValue(point.garbageTypes)
        pointDB.child(LATITUDE).setValue(point.latitude)
        pointDB.child(LONGITUDE).setValue(point.longitude)
        if (point.size == Point.BIG){
            pointDB.child(START_TIME).setValue(point.startTime)
            pointDB.child(END_TIME).setValue(point.endTime)
        }
    }

    fun deletePoint(point: Point){
        points.child(when(point.size) {
            Point.BIG -> "Big"
            Point.SMALL -> "Small"
            else -> return
        }).child(point.id).removeValue()
    }
}