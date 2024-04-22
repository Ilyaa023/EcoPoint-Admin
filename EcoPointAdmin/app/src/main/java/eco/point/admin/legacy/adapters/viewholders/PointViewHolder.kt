package eco.point.admin.legacy.adapters.viewholders

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import eco.point.admin.R
import eco.point.admin.databinding.PointItemBinding
import eco.point.admin.legacy.elements.Point
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.firebase.data.FBConnect
import java.lang.Exception

class PointViewHolder(item: View): RecyclerView.ViewHolder(item) {
    private var binding: PointItemBinding
    private var isBig = true
    private var fbConnect: FBConnect
    private lateinit var bigRB: RadioButton
    private lateinit var smallRB: RadioButton
    private lateinit var addressET: EditText
    private lateinit var description: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var org: EditText
    private lateinit var startTime: EditText
    private lateinit var endTime: EditText
    private lateinit var completeBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var plastic: CheckBox
    private lateinit var glass: CheckBox
    private lateinit var metal: CheckBox
    private lateinit var paper: CheckBox
    private lateinit var food: CheckBox
    private lateinit var optionll: LinearLayout
    init {
        binding = PointItemBinding.bind(item)
        fbConnect = FBConnect()
    }

    fun bind(point: Point, activity: Activity) {
        binding.pointIcon.setImageResource(if(point.getSize() == DataType.SMALL_POINTS.data) R.drawable.ic_point_small else R.drawable.ic_point_big)
        binding.pointId.text = "id: ${point.getID()}"
        binding.pointAddress.text = point.getAddress()
        binding.pointOrganization.text = point.getOrganization()

        binding.pointCardView.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(it.context)
            val constraintLayout =activity.layoutInflater.inflate(R.layout.alert_dialog_layout_points, null)
            builder.setView(constraintLayout)
            val dialog = builder.show()
            bigRB= constraintLayout.findViewById(R.id.Big)
            smallRB= constraintLayout.findViewById(R.id.Small)
            addressET= constraintLayout.findViewById(R.id.addr)
            description= constraintLayout.findViewById(R.id.description)
            latitude= constraintLayout.findViewById(R.id.latitude)
            longitude= constraintLayout.findViewById(R.id.longitude)
            org= constraintLayout.findViewById(R.id.organization)
            startTime= constraintLayout.findViewById(R.id.startTime)
            endTime = constraintLayout.findViewById(R.id.endTime)
            completeBtn= constraintLayout.findViewById(R.id.add)
            deleteBtn= constraintLayout.findViewById(R.id.delete)
            plastic = constraintLayout.findViewById(R.id.plastic)
            glass = constraintLayout.findViewById(R.id.glass)
            metal = constraintLayout.findViewById(R.id.metal)
            paper = constraintLayout.findViewById(R.id.paper)
            food = constraintLayout.findViewById(R.id.food)
            optionll = constraintLayout.findViewById(R.id.optionll)

            bigRB.isChecked = point.getSize() == DataType.BIG_POINTS.data
            smallRB.isChecked = point.getSize() == DataType.SMALL_POINTS.data
            optionll.visibility = if (bigRB.isChecked) View.VISIBLE else View.GONE
            bigRB.setOnClickListener(radioButtonClickListener)
            smallRB.setOnClickListener(radioButtonClickListener)

            addressET.setText(point.getAddress())
            description.setText(point.getDescription())
            latitude.setText(point.getLatitude())
            longitude.setText(point.getLongitude())
            org.setText(point.getOrganization())
            try {
                startTime.setText(point.getStartTime())
                endTime.setText(point.getEndTime())
            }catch (e: Exception){}
            setCBTAG(point.getTAG())

            completeBtn.setOnClickListener {
                point.setAddress(addressET.text.toString())
                point.setDescription(description.text.toString())
                point.setOrganization(org.text.toString())
                point.setLatitude(latitude.text.toString())
                point.setLongitude(longitude.text.toString())
                if((point.getSize() == DataType.BIG_POINTS.data) != isBig){
                    fbConnect.deletePoint(point.getID(), point.getSize())
                    point.setSize(if (isBig) DataType.BIG_POINTS.data else DataType.SMALL_POINTS.data)
                }
                point.setTAG(getCBTAG())
                try {
                    point.setStartTime(startTime.text.toString())
                    point.setEndTime(endTime.text.toString())
                }catch (e: Exception){}
                fbConnect.sendPoint(point)
                dialog.dismiss()
            }
            deleteBtn.setOnLongClickListener {
                fbConnect.deletePoint(point.getID(), point.getSize())
                dialog.dismiss()
                return@setOnLongClickListener false
            }
        })
    }

    fun setCBTAG(tag: String){
        if (tag.contains("plastic"))
            plastic.isChecked = true
        if (tag.contains("glass"))
            glass.isChecked = true
        if (tag.contains("metal"))
            metal.isChecked = true
        if (tag.contains("paper"))
            paper.isChecked = true
        if (tag.contains("food"))
            food.isChecked = true
    }

    fun getCBTAG(): String {
        var summTAG = ""
        if (plastic.isChecked) {
            summTAG += "plastic "
        }
        if (glass.isChecked) {
            summTAG += "glass "
        }
        if (metal.isChecked) {
            summTAG += "metal "
        }
        if (paper.isChecked) {
            summTAG += "paper "
        }
        if (food.isChecked) {
            summTAG += "food "
        }
        return summTAG
    }

    var radioButtonClickListener =
        View.OnClickListener { v ->
            val rb = v as RadioButton
            when (rb.id) {
                R.id.Big -> {
                    optionll.setVisibility(View.VISIBLE)
                    isBig = true
                }
                R.id.Small -> {
                    optionll.setVisibility(View.GONE)
                    isBig = false
                }
                else -> {
                }
            }
        }
}
