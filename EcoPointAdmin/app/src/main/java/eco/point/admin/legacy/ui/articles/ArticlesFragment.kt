package eco.point.admin.legacy.ui.articles

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import eco.point.admin.R
import eco.point.admin.legacy.adapters.ArticleAdapter
import eco.point.admin.databinding.FragmentArticlesBinding
import eco.point.admin.legacy.elements.Article
import eco.point.admin.legacy.enums.ArticlesData
import eco.point.admin.legacy.enums.DataType
import eco.point.admin.legacy.firebase.data.FBCallback
import eco.point.admin.legacy.firebase.data.FBConnect
import eco.point.admin.legacy.firebase.data.IFBSCallback
import eco.point.admin.legacy.ui.ArticleCreatorActivity

class ArticlesFragment : Fragment(), FBCallback {
    private lateinit var binding: FragmentArticlesBinding
    private lateinit var layoutManager: LinearLayoutManager
    private var articles = ArrayList<Article>()
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticlesBinding.inflate(layoutInflater)
        val root = binding.root
        layoutManager = LinearLayoutManager(context)
        val fbConnect = FBConnect()
        fbConnect.StartEventListener(DataType.ARTICLES, this)
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@ArticlesFragment.context, ArticleCreatorActivity::class.java)
            intent.putExtra("add", true)
            startActivity(intent)
        }
        return root
    }

    override fun onResult(result: DataSnapshot) {
        articles.clear()
        for (snapshot: DataSnapshot in result.children){
            val id = snapshot.key.toString()
            val name = snapshot.child(ArticlesData.NAME.data).value.toString()
            val steps = ArrayList<String>()
            val texts = ArrayList<String>()
            val public = snapshot.child(ArticlesData.PUBLIC.data).value.toString().toBoolean()
            for (snap: DataSnapshot in snapshot.child(ArticlesData.STEPS.data).children){
                steps.add(snap.key!!.toInt(), snapshot.child(ArticlesData.STEPS.data).child(snap.key.toString()).value.toString())
                texts.add(snap.key!!.toInt(), snapshot.child(ArticlesData.TEXTS.data).child(snap.key.toString()).value.toString())
            }
            articles.add(
                Article(
                    name = name,
                    id = id,
                    mainPic = null,
                    steps = steps,
                    public = public,
                    texts = texts
                )
            )
            FBConnect().getArticlesMainPic(articles, object : IFBSCallback {
                override fun onResult(articlesList: ArrayList<Article>) {
                    /*for (num: Int in 0..articles.size-1){
                        articles[num].mainPic =
                    }*/
                    /*binding.articlesRecycle.setHasFixedSize(false)
                    binding.articlesRecycle.layoutManager = layoutManager
                    adapter = ArticleAdapter(articles, activity!!)
                    binding.articlesRecycle.adapter = adapter*/
                    //articles.clear()
                    try {
                        if (articlesList.size > 0){
                            binding.articlesProgressBar.visibility = View.GONE
                        }
                        for (button: View in binding.articlesView.children){
                            binding.articlesView.removeView(button)
                        }
                        val dpW = resources.displayMetrics.widthPixels / 411f
                        for (article: Article in articlesList) {
                            //articles.add(article)
                            val button = Button(context)

                            val lp = LinearLayout.LayoutParams((367 * dpW).toInt(), (91 * dpW).toInt())
                            val tt = (22 * dpW).toInt()
                            lp.setMargins(tt, tt, tt, 0)
                            button.layoutParams = lp
                            button.setPadding((134 * dpW).toInt(), 0, (29 * dpW).toInt(), 0)
                            button.setTextColor(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.white,
                                    null
                                )
                            )
                            button.textSize = 12F
                            button.text = article.name
                            val bitmap = article.mainPic!!
                            val res: Bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
                            val mask = BitmapFactory.decodeResource(resources, R.drawable.ic_article_mask).extractAlpha()
                            val canvas = Canvas(res)
                            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
                            canvas.drawBitmap(bitmap, 0f, 0f, null)
                            canvas.drawBitmap(mask, 0f, 0f, paint)
                            paint.xfermode = null
                            paint.style = Paint.Style.STROKE
                            button.background = BitmapDrawable(resources, res)
                            button.setOnClickListener { b ->
                                val popupMenu = PopupMenu(activity, b)
                                popupMenu.inflate(R.menu.articles_popup_menu)
                                popupMenu.setOnMenuItemClickListener {
                                    when (it.itemId){
                                        R.id.preview -> {
                                            startActivity(Intent())
                                            true
                                        }
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
                                            activity!!.startActivity(intent)
                                            true
                                        }
                                        R.id.delete -> {
                                            FBConnect().deleteArticle(article.id)
                                            true
                                        }
                                        else -> true
                                    }
                                }
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
    //                            val intent = Intent(context, ArticleActivity::class.java)
    //                            intent.putStringArrayListExtra("Steps", article.steps)
    //                            intent.putStringArrayListExtra("Texts", article.texts)
    //                            intent.putExtra("id", article.id)
    //                            startActivity(intent)
                            }
                            binding.articlesView.addView(button)
                        }
                    } catch (e: Exception){}
                }
            })
        }
    }

    override fun onFailrule() {

    }
}