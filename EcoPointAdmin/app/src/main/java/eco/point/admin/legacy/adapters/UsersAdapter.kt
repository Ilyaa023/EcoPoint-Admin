package eco.point.admin.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import eco.point.admin.R
import eco.point.admin.legacy.adapters.viewholders.UserViewHolder
import eco.point.admin.legacy.elements.User
import java.util.ArrayList

class UsersAdapter(dataSnapshot: DataSnapshot): RecyclerView.Adapter<UserViewHolder>() {
    private var data = ArrayList<User>()

    init {
        data = getData(dataSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    private fun getData(dataSnapshot: DataSnapshot): ArrayList<User> {
        val list = ArrayList<User>()
        for(snapshot: DataSnapshot in dataSnapshot.children){
            list.add(User(snapshot))
        }
        return list
    }
}