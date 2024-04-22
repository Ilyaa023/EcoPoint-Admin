package eco.point.admin.legacy.ui.points

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import eco.point.admin.R
import eco.point.admin.legacy.adapters.PointsAdapter
import eco.point.admin.databinding.FragmentPointsBinding
import eco.point.admin.legacy.elements.Point
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.firebase.data.FBCallback
import eco.point.admin.legacy.firebase.data.FBConnect
import java.lang.Exception
import java.util.*

class PointsFragment : Fragment(), FBCallback {
    private var isBig = false
    private lateinit var binding: FragmentPointsBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PointsAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointsBinding.inflate(inflater, container, false)
        val root = binding.root
        layoutManager = LinearLayoutManager(context)
        val fbConnect = FBConnect()
        fbConnect.StartEventListener(DataType.POINTS, this)
        val fab = root.findViewById<FloatingActionButton>(R.id.pointsFAB)
        fab.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            val constraintLayout = layoutInflater.inflate(R.layout.alert_dialog_layout_points, null)
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
            deleteBtn.visibility = View.GONE
            optionll.visibility = View.GONE
            smallRB.isChecked = true
            bigRB.setOnClickListener(radioButtonClickListener)
            smallRB.setOnClickListener(radioButtonClickListener)

            completeBtn.setOnClickListener {
                val point = Point(null, if (isBig) DataType.BIG_POINTS else DataType.SMALL_POINTS)
                val calendar = GregorianCalendar()
                point.setID(calendar.timeInMillis.toString())
                point.setAddress(addressET.text.toString())
                point.setDescription(description.text.toString())
                point.setOrganization(org.text.toString())
                point.setLatitude(latitude.text.toString())
                point.setLongitude(longitude.text.toString())
                point.setTAG(getCBTAG())
                try {
                    point.setStartTime(startTime.text.toString())
                    point.setEndTime(endTime.text.toString())
                }catch (e: Exception){}
                fbConnect.sendPoint(point)
                dialog.dismiss()
            }
        }
        return root
    }

    override fun onResult(result: DataSnapshot) {
        binding.pointsRecycle.setHasFixedSize(false)
        binding.pointsRecycle.layoutManager = layoutManager
        adapter = PointsAdapter(result, requireActivity())
        binding.pointsRecycle.adapter = adapter
    }

    override fun onFailrule() {
    }

    private fun getCBTAG(): String {
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

    private var radioButtonClickListener =
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