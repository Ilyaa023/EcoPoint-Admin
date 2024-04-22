package eco.point.admin.legacy.ui.wrongPoints

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import eco.point.admin.legacy.adapters.TelegramAdapter
import eco.point.admin.legacy.adapters.viewholders.TelegramAlert
import eco.point.admin.databinding.FragmentWrongPointsBinding
import eco.point.admin.legacy.elements.Telegram
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.firebase.data.FBCallback
import eco.point.admin.legacy.firebase.data.FBConnect
import java.lang.Exception

class WrongPointsFragment: Fragment(), FBCallback, TelegramAlert {
    private lateinit var binding: FragmentWrongPointsBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: TelegramAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWrongPointsBinding.inflate(inflater, container, false)
        layoutManager = LinearLayoutManager(context)
        val fbConnect = FBConnect()
        fbConnect.StartEventListener(DataType.WRONG_POINTS, this)
        return binding.root
    }

    override fun onResult(result: DataSnapshot) {
        binding.wrongPointsRecycler.setHasFixedSize(false)
        binding.wrongPointsRecycler.layoutManager = layoutManager
        adapter = TelegramAdapter(result, DataType.WRONG_POINTS, this)
        binding.wrongPointsRecycler.adapter = adapter
    }

    override fun onFailrule() {
    }

    override fun startTelegram(t: Telegram) {
        try {
            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${t.getTgID()}"))
            Log.i("startTlg: ", "startTlg: https://t.me/${t.getTgID()}")
            telegram.setPackage("org.telegram.messenger")
            startActivity(telegram)
        }catch (e: Exception){
            Toast.makeText(this@WrongPointsFragment.context, "Telegram app is not installed", Toast.LENGTH_LONG).show();
        }
    }
}