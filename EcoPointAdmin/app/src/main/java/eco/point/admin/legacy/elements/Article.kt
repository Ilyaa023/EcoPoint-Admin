package eco.point.admin.legacy.elements

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Article(
        var name: String = "defName",
        var id:  String = "${FirebaseAuth.getInstance().currentUser!!.uid}_${GregorianCalendar().timeInMillis}",
        var mainPic: Bitmap?,
        var steps: ArrayList<String> = ArrayList<String>(),
        var pictures: ArrayList<Bitmap> = ArrayList<Bitmap>(),
        var texts: ArrayList<String> = ArrayList<String>(),
        var public: Boolean = false
): Serializable {
}