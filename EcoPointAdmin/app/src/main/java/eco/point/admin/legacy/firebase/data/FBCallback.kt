package eco.point.admin.legacy.firebase.data

import com.google.firebase.database.DataSnapshot

interface FBCallback {
    fun onResult(result: DataSnapshot)
    fun onFailrule()
}