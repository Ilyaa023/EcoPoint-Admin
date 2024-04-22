package eco.point.admin.legacy.adapters.viewholders

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import eco.point.admin.R
import eco.point.admin.databinding.ArticleItemBinding
import eco.point.admin.legacy.elements.Article
import eco.point.admin.legacy.firebase.data.FBConnect
import eco.point.admin.legacy.ui.ArticleCreatorActivity

class ArticleViewHolder(item: View): RecyclerView.ViewHolder(item) {
    private val binding: ArticleItemBinding

    init {
        binding = ArticleItemBinding.bind(item)
    }

    fun bind(article: Article, activity: Activity) {
        binding.pubState.text = if (article.public) "Public" else "Private"
        binding.pubState.setTextColor(if (article.public) Color.BLUE else Color.GREEN)
        binding.butt.background = BitmapDrawable(article.mainPic)
        binding.butt.text = article.name

        val popupMenu = PopupMenu(activity, binding.butt)
        popupMenu.inflate(R.menu.articles_popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId){
//                R.id.preview -> {
//                    // TODO: startActivity for preview
//                    true
//                }
                R.id.pub -> {
                    article.public = !article.public
                    FBConnect().sendArticle(article)
                    true
                }
                R.id.edit -> {
                    val intent = Intent(activity, ArticleCreatorActivity::class.java)
                    intent.putExtra("add", false)
                    intent.putExtra("ArticlePublic", article.public)
                    intent.putExtra("ArticleID", article.id)
                    intent.putStringArrayListExtra("ArticleSteps", article.steps)
                    intent.putExtra("ArticleName", article.name)
                    intent.putStringArrayListExtra("ArticleTexts", article.texts)
                    activity.startActivity(intent)
                    true
                }
                R.id.delete -> {

                    FBConnect().deleteArticle(article.id)
                    true
                }
                else -> true
            }
        }
        binding.butt.setOnClickListener {
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu,true)
            }
            catch (e: Exception)
            {
                Log.d("error", e.toString())
            }
            finally {
                popupMenu.show()
            }
        }
    }
}