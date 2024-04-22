package eco.point.admin.legacy.firebase.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import eco.point.admin.legacy.elements.Article
import eco.point.admin.legacy.elements.Point
import eco.point.admin.legacy.enums.ArticlesData
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.enums.PointData
import java.io.ByteArrayOutputStream
import kotlin.Exception

class FBConnect {
    private val TAG = javaClass.name
    private val database = FirebaseDatabase.getInstance()
    private val points = database.getReference(DataType.POINTS.data)
    private val users = database.getReference(DataType.USERS.data)
    private val telegram = database.getReference("telegram")
    private val wrongPoints = telegram.child(DataType.WRONG_POINTS.data)
    private val newPoints = telegram.child(DataType.NEW_POINTS.data)
    private val questions = telegram.child(DataType.USERS_QUESTIONS.data)
    private val bugs = telegram.child(DataType.BUGS.data)
    private val articles = database.getReference(DataType.ARTICLES.data)

    private val articlesPicStore = Firebase.storage("gs://my-ecopoint-project.appspot.com").getReference("Articles")

    fun StartEventListener(dataType: DataType, fbCallback: FBCallback){
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onDataChange: $dataSnapshot", )
                fbCallback.onResult(dataSnapshot)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                fbCallback.onFailrule()
            }
        }
        when(dataType){
            DataType.POINTS -> points.addValueEventListener(postListener)
            DataType.NEW_POINTS -> newPoints.addValueEventListener(postListener)
            DataType.WRONG_POINTS -> wrongPoints.addValueEventListener(postListener)
            DataType.USERS -> users.addValueEventListener(postListener)
            DataType.USERS_QUESTIONS -> questions.addValueEventListener(postListener)
            DataType.BUGS -> bugs.addValueEventListener(postListener)
            DataType.ARTICLES -> articles.addValueEventListener(postListener)
        }
    }

    fun sendPoint(point: Point){
        val currPoint = points.child(point.getSize())
        currPoint.child(point.getID()).child(PointData.ADDRESS.data).setValue(point.getAddress())
        currPoint.child(point.getID()).child(PointData.DESCRIPTION.data).setValue(point.getDescription())
        currPoint.child(point.getID()).child(PointData.ORGANIZATION.data).setValue(point.getOrganization())
        currPoint.child(point.getID()).child(PointData.LATITUDE.data).setValue(point.getLatitude())
        currPoint.child(point.getID()).child(PointData.LONGITUDE.data).setValue(point.getLongitude())
        currPoint.child(point.getID()).child(PointData.TAG.data).setValue(point.getTAG())
        currPoint.child(point.getID()).child(PointData.PHOTO.data).setValue("empty")
        currPoint.child(point.getID()).child(PointData.POINT_RATING.data).setValue("empty")
        try{
            currPoint.child(point.getID()).child(PointData.START_TIME.data).setValue(point.getStartTime())
            currPoint.child(point.getID()).child(PointData.END_TIME.data).setValue(point.getEndTime())
        }catch (e: Exception){}
    }

    fun deletePoint(id: String, size: String){
        points.child(size).child(id).removeValue()
    }

    fun deleteTelegramMessage(dataType: DataType, id: String){
        telegram.child(dataType.data).child(id).removeValue()
    }

    fun sendArticle(article: Article){
        articles.child(article.id).child(ArticlesData.NAME.data).setValue(article.name)
        try{
            val byteArrayOutputStream = ByteArrayOutputStream()
                article.mainPic!!.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                )
            val byteArray = byteArrayOutputStream.toByteArray()
            articlesPicStore.child(article.id).child(ArticlesData.MAIN_PIC.data).child("pic.jpg").putBytes(byteArray)
        } catch (e:Exception){
            e.printStackTrace()
        }
        for (counter: Int in 0..article.steps.size - 1){
            val byteArrayOutputStream = ByteArrayOutputStream()
            try {
                article.pictures[counter].compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                )
                val byteArray = byteArrayOutputStream.toByteArray()
                Log.e(
                    TAG,
                    "sendArticle: ${
                        articlesPicStore.child(article.id).child(ArticlesData.PICTURES.data)
                            .child(counter.toString()).child("pic.jpg")
                    }",
                )
                val uploadTask =
                    articlesPicStore.child(article.id).child(ArticlesData.PICTURES.data)
                        .child(counter.toString()).child("pic.jpg").putBytes(byteArray)
            }catch (e: Exception){}
            articles.child(article.id).child(ArticlesData.STEPS.data).child(counter.toString()).setValue(article.steps[counter])
            try {
                articles.child(article.id).child(ArticlesData.TEXTS.data).child(counter.toString())
                    .setValue(article.texts[counter])
            }catch (e: Exception){}
        }
        articles.child(article.id).child(ArticlesData.PUBLIC.data).setValue(article.public)
    }

    fun getArticlesMainPic(articlesList: ArrayList<Article>, callback: IFBSCallback){
        for (num: Int in 0..articlesList.size-1){
            articlesPicStore.child(articlesList[num].id).child(ArticlesData.MAIN_PIC.data).child("pic.jpg").getBytes(
                Long.MAX_VALUE).addOnCompleteListener{
                try {
                    articlesList[num].mainPic =
                        BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                }catch (e:Exception){}
                if (num == articlesList.size - 1)
                    callback.onResult(articlesList)
            }
        }
    }

    fun getArticlesContentPic(articlesList: ArrayList<Article>, callback: IFBSCallback){
        for (n: Int in 0..articlesList.size - 1) {
            articlesList[n].pictures = ArrayList<Bitmap>()
            for (counter: Int in 0..articlesList[n].steps.size - 1) {
                articlesPicStore.child(articlesList[n].id)
                    .child(ArticlesData.PICTURES.data).child(counter.toString())
                    .child("pic.jpg").getBytes(
                        Long.MAX_VALUE
                    ).addOnCompleteListener {
                        try {
                            articlesList[n].pictures.add(
                                counter, BitmapFactory.decodeByteArray(
                                    it.result, 0, it.result.size
                                )
                            )
                        } catch (e: Exception){}
                        if (n == articlesList.size - 1 && counter == articlesList[n].steps.size - 1)
                            callback.onResult(articlesList)
                    }
            }
        }
    }

    fun getArticlesAllPic(articlesList: ArrayList<Article>, callback: IFBSCallback){
        try {
            for (num: Int in 0..articlesList.size - 1) {
                articlesPicStore.child(articlesList[num].id).child(ArticlesData.MAIN_PIC.data)
                    .child("pic.jpg").getBytes(
                    Long.MAX_VALUE
                ).addOnCompleteListener {
                    try {
                        articlesList[num].mainPic =
                            BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    } catch (e: Exception){e.printStackTrace()}
                    finally {
                        if (num == articlesList.size - 1) {
                            for (n: Int in 0..articlesList.size - 1) {
                                articlesList[n].pictures = ArrayList<Bitmap>()
                                var origSum = 0
                                var sum = 0
                                for (counter: Int in 0..articlesList[n].steps.size - 1) {
                                    origSum += counter
                                    articlesList[n].pictures.add(counter, articlesList[n].mainPic!!)
                                    articlesPicStore.child(articlesList[n].id)
                                        .child(ArticlesData.PICTURES.data).child(counter.toString())
                                        .child("pic.jpg").getBytes(
                                            Long.MAX_VALUE
                                        ).addOnCompleteListener {
                                            Log.e(TAG, "getArticlesAllPic: counter $counter", )
                                            sum += counter
                                            try {
                                                articlesList[n].pictures.set(
                                                    counter, BitmapFactory.decodeByteArray(
                                                        it.result, 0, it.result.size
                                                    )
                                                )
                                            } catch (e: Exception) {
                                            }
                                            if (n == articlesList.size - 1 && sum == origSum)
                                                callback.onResult(articlesList)
                                        }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception){e.printStackTrace()}
    }

    fun deleteArticle(id: String){
        articles.child(id).removeValue()
        articlesPicStore.child(id).child(ArticlesData.MAIN_PIC.data)
            .child("pic.jpg").delete()
        var b = true
        var c = 0
        while (b && c < 20) {
            try {
                Log.e(TAG, "deleteArticle: ${c}")
                articlesPicStore.child(id).child(ArticlesData.PICTURES.data).child(c.toString())
                    .child("pic.jpg").delete()
                c++
            } catch (e: Exception){
                b = false
            }
        }
    }
}