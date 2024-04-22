package eco.point.admin.legacy.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eco.point.admin.R
import eco.point.admin.legacy.adapters.viewholders.ArticleViewHolder
import eco.point.admin.legacy.elements.Article

class ArticleAdapter(private val articles: ArrayList<Article>, val activity: Activity): RecyclerView.Adapter<ArticleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position], activity)
    }

    override fun getItemCount(): Int = articles.size
}