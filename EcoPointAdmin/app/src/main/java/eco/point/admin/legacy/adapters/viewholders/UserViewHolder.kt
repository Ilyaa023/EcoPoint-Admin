package eco.point.admin.legacy.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eco.point.admin.databinding.UserItemBinding
import eco.point.admin.legacy.elements.User
import eco.point.admin.legacy.firebase.data.FBConnect
import java.util.*

class UserViewHolder(item: View): RecyclerView.ViewHolder(item) {
    private val binding: UserItemBinding
    private val fbConnect: FBConnect

    init {
        binding = UserItemBinding.bind(item)
        fbConnect = FBConnect()
    }

    fun bind(user: User){
        binding.userName.text = user.getName()
        binding.userCity.text = user.getCity()
        binding.userLevel.text = user.getLevel()
        binding.userBonuses.text = user.getBonuses()
        binding.userGarbageCounter.text = user.getGarbageCounter()
        val calendar = user.getBirthDate()
        val month: Int = calendar.get(Calendar.MONTH) + 1
        binding.userBirth.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()}.$month.${calendar.get(Calendar.YEAR)}"
    }
}