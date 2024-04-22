package eco.point.admin.legacy.adapters.viewholders

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eco.point.admin.databinding.TelegramItemBinding
import eco.point.admin.legacy.elements.Telegram
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.firebase.data.FBConnect


class TelegramViewHolder(item: View): RecyclerView.ViewHolder(item) {
    private var binding: TelegramItemBinding

    init {
        binding = TelegramItemBinding.bind(item)
    }

    fun bind(telegram: Telegram, type: DataType, telegramAlert: TelegramAlert){
        binding.telegramUserid.text = telegram.getID()
        binding.telegramUserActionName.text =
                when (type){
                    DataType.BUGS -> "Баг:"
                    DataType.USERS_QUESTIONS -> "Вопрос:"
                    DataType.NEW_POINTS -> "Новый пункт:"
                    DataType.WRONG_POINTS -> "Неработающий пункт:"
                    else -> "Error"
                }
        //Log.e("TAG", "bind: ${telegramAlert}", )
        binding.telegramUserAction.text = telegram.getMessage()
        binding.telegramCard.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle(telegram.getID())
                .setMessage(telegram.getMessage())
                .setPositiveButton("Open in Telegram"){
                        dialog, id ->
                        telegramAlert.startTelegram(telegram)
                        dialog.cancel()
                }
                .setNegativeButton("Delete Message"){
                        dialog, id ->
                        val fbConnect = FBConnect()
                        fbConnect.deleteTelegramMessage(type, telegram.getID())
                        dialog.cancel()
                }
            builder.show()
        }
    }
    /*fun startTlg(t: Telegram){
        val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${t.getID()}"))
        Log.i("startTlg: ", "startTlg: https://t.me/${t.getID()}")
        telegram.setPackage("org.telegram.messenger")
        startActivity(telegram)
    }*/
}