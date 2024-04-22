package eco.point.admin.legacy.elements

import com.google.firebase.database.DataSnapshot
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.enums.PointData

class Point(dataSnapshot: DataSnapshot?, dataType: DataType) {
    private lateinit var id: String
    private lateinit var address: String
    private lateinit var size: String
    private lateinit var description: String
    private lateinit var organization: String
    private lateinit var garbageTypes: String
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var startTime: String
    private lateinit var endTime: String
    init {
        try {
            size = dataType.data
            id = dataSnapshot?.key.toString()
            for(data: DataSnapshot in dataSnapshot!!.children){
                when(data.key){
                    PointData.ADDRESS.data -> address = data.value.toString()
                    PointData.DESCRIPTION.data -> description = data.value.toString()
                    PointData.END_TIME.data -> endTime = data.value.toString()
                    PointData.START_TIME.data -> startTime = data.value.toString()
                    PointData.ORGANIZATION.data -> organization = data.value.toString()
                    PointData.LATITUDE.data -> latitude = data.value.toString()
                    PointData.LONGITUDE.data -> longitude = data.value.toString()
                    PointData.TAG.data -> garbageTypes = data.value.toString()
                }
            }
        }catch(e: Exception){}
    }

    fun getSize(): String = size
    fun getID(): String = id
    fun getAddress(): String = address
    fun getDescription(): String = description
    fun getOrganization(): String = organization
    fun getTAG(): String = garbageTypes
    fun getLatitude(): String = latitude
    fun getLongitude(): String = longitude
    fun getStartTime(): String = startTime
    fun getEndTime(): String = endTime

    fun setSize(s: String) {
        size = s
    }
    fun setID(s: String){
        id = s
    }
    fun setAddress(s: String){
        address = s
    }
    fun setDescription(s: String){
        description = s
    }
    fun setOrganization(s: String){
        organization = s
    }
    fun setTAG(s: String){
        garbageTypes = s
    }
    fun setLatitude(s: String){
        latitude = s
    }
    fun setLongitude(s: String){
        longitude = s
    }
    fun setStartTime(s: String){
        startTime = s
    }
    fun setEndTime(s: String){
        endTime = s
    }
}