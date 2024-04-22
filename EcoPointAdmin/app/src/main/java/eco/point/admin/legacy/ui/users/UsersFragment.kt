package eco.point.admin.legacy.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import eco.point.admin.legacy.adapters.UsersAdapter
import eco.point.admin.databinding.FragmentUsersBinding
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.firebase.data.FBCallback
import eco.point.admin.legacy.firebase.data.FBConnect

class UsersFragment : Fragment(), FBCallback {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        layoutManager = LinearLayoutManager(context)
        val fbConnect = FBConnect()
        fbConnect.StartEventListener(DataType.USERS, this)
        return binding.root
    }

    override fun onResult(result: DataSnapshot) {
        binding.usersRecycle.setHasFixedSize(false)
        binding.usersRecycle.layoutManager = layoutManager
        adapter = UsersAdapter(result)
        binding.usersRecycle.adapter = adapter
    }

    override fun onFailrule() {

    }
}