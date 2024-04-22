package eco.point.admin.legacy.ui.articles

import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import eco.point.admin.databinding.ActivityArticleBinding
import eco.point.admin.legacy.elements.Article

class ArticleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var article: Article
    private val stepsList = ArrayList<RadioButton>()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val articleVM = ViewModelProvider(this)[ArticleActivityViewModel::class.java]
        val arguments = intent.extras
        val id = arguments?.getString("id")
        val steps = arguments?.getStringArrayList("Steps")
        val texts = arguments?.getStringArrayList("Texts")
        article = Article(
            id = id!!,
            steps = steps!!,
            texts = texts!!
        )
        binding.stepsGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                val currStep = stepsList.indexOf(findViewById(p1))
                binding.contentView.setImageBitmap(articleVM.getBitmap(article.pictures[currStep]!!))
                binding.textView.text = article.texts[currStep]
                changeRBColors()
            }
        })
        articleVM.getArticlesPics(article){
            article.pictures = it.pictures
            binding.progressBar.visibility = View.GONE
            for (num: Int in 0 until article.steps.size){
                val rb = RadioButton(this)
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.setMargins(0,10,0, 10)
                rb.layoutParams = lp
                rb.gravity = Gravity.CENTER_VERTICAL
                rb.setPadding(16, 0, 0, 0)
                rb.text = steps[num]
                rb.setTextColor(getColor(R.color.green))
                rb.buttonDrawable = getDrawable(R.drawable.ic_green_rb)
                stepsList.add(num, rb)
                binding.stepsGroup.addView(rb)
                if (num == 0){
                    rb.isChecked = true
                }
            }
            binding.leftButton.setOnClickListener {
                val currStep = stepsList.indexOf(findViewById<RadioButton>(binding.stepsGroup.checkedRadioButtonId))
                if (currStep > 0)
                    stepsList[currStep - 1].isChecked = true
            }
            binding.rightButton.setOnClickListener {
                val currStep = stepsList.indexOf(findViewById<RadioButton>(binding.stepsGroup.checkedRadioButtonId))
                if (currStep < stepsList.size - 1)
                    stepsList[currStep + 1].isChecked = true
            }
        }
    }

    private fun changeRBColors(){
        for (rb: RadioButton in stepsList){
            if (rb.isChecked) {
                rb.buttonDrawable = getDrawable(R.drawable.ic_dark_green_rb)
                rb.setTextColor(getColor(R.color.dark_green))
            }
            else {
                rb.buttonDrawable = getDrawable(R.drawable.ic_green_rb)
                rb.setTextColor(getColor(R.color.green))
            }
        }
    }*/
}