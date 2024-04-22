package eco.point.admin.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import eco.point.admin.R
import eco.point.admin.legacy.adapters.viewholders.TelegramAlert
import eco.point.admin.legacy.adapters.viewholders.TelegramViewHolder
import eco.point.admin.legacy.elements.Telegram
import eco.point.admin.legacy.enums.DataType
import java.util.ArrayList

class TelegramAdapter(dataSnapshot: DataSnapshot, private val type: DataType, private  val telegramAlert: TelegramAlert): RecyclerView.Adapter<TelegramViewHolder>() {
    private var data = ArrayList<Telegram>()

    init {
        data = getData(dataSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelegramViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.telegram_item, parent, false)
        return TelegramViewHolder(view)
    }

    override fun onBindViewHolder(holder: TelegramViewHolder, position: Int) {
        holder.bind(data[position], type, telegramAlert)
    }

    override fun getItemCount(): Int = data.size

    private fun getData(dataSnapshot: DataSnapshot): ArrayList<Telegram> {
        val list = ArrayList<Telegram>()
        for(snapshot: DataSnapshot in dataSnapshot.children)
            list.add(Telegram(snapshot))
        return list
    }
}