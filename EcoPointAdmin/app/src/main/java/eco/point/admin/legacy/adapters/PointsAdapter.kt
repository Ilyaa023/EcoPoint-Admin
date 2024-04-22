package eco.point.admin.legacy.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import eco.point.admin.R
import eco.point.admin.legacy.adapters.viewholders.PointViewHolder
import eco.point.admin.legacy.elements.Point
import eco.point.admin.legacy.enums.DataType
import java.util.ArrayList

class PointsAdapter(dataSnapshot: DataSnapshot, val activity: Activity): RecyclerView.Adapter<PointViewHolder>() {
    private var data = ArrayList<Point>()

    init {
        data = getData(dataSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.point_item, parent, false)
        return PointViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(data[position], activity)
    }

    override fun getItemCount(): Int = data.size

    private fun getData(dataSnapshot: DataSnapshot): ArrayList<Point> {
        val list = ArrayList<Point>()
        for(snapshot: DataSnapshot in dataSnapshot.children){
            when(snapshot.key){
                DataType.SMALL_POINTS.data -> {
                    for (ds: DataSnapshot in snapshot.children)
                        list.add(Point(ds, DataType.SMALL_POINTS))
                }
                DataType.BIG_POINTS.data -> {
                    for (ds: DataSnapshot in snapshot.children)
                        list.add(Point(ds, DataType.BIG_POINTS))
                }
            }
        }
        return list
    }
}