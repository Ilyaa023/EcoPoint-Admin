package eco.point.admin.legacy.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import eco.point.admin.R
import eco.point.admin.databinding.ActivityArticleCreatorBinding
import eco.point.admin.databinding.LayoutArticleDataBinding
import eco.point.admin.legacy.elements.Article
import eco.point.admin.legacy.firebase.data.FBConnect
import eco.point.admin.legacy.firebase.data.IFBSCallback


class ArticleCreatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleCreatorBinding
    private lateinit var galleryResultHandler: ActivityResultLauncher<Intent>
    private val pictures = ArrayList<Bitmap>()
    private val steps = ArrayList<RadioButton>()
    private val texts = ArrayList<String>()
    private lateinit var rbToDel: RadioButton
    private lateinit var defBmp: Bitmap
    private val mainPic = MutableLiveData<Bitmap>()

    private var add = true
    private lateinit var originalArticle: Article
    private var currIndex = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val arguments = intent.extras
            add = arguments!!.getBoolean("add")
            Log.e("TAG", "onCreate: add: $add", )
            if (!add) {
                val originalArticleID = arguments.getString("ArticleID")
                val originalArticleSteps = arguments.getStringArrayList("ArticleSteps")
                val originalArticlePublic = arguments.getBoolean("ArticlePublic")
                val originalArticleName = arguments.getString("ArticleName")
                val originalArticleTexts = arguments.getStringArrayList("ArticleTexts")
                Log.e("TAG", "onCreate: articleID: $originalArticleID", )
                FBConnect().getArticlesAllPic(arrayListOf(Article(id = originalArticleID!!, mainPic = null, steps = originalArticleSteps!!)), object :
                        IFBSCallback {
                    override fun onResult(articlesList: ArrayList<Article>) {
                        Log.e("TAG", "onCreate: article!", )
                        originalArticle = articlesList[0]
                        originalArticle.name = originalArticleName!!
                        originalArticle.public = originalArticlePublic
                        originalArticle.texts = originalArticleTexts!!
                        initEdit()
                    }
                })
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        defBmp = BitmapFactory.decodeResource(resources, android.R.drawable.ic_menu_gallery)
        if (add) makeAlertDialog(0)
        galleryResultHandler = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            var bitmap: Bitmap? = null
            if(it.resultCode == RESULT_OK) {
                val selectedImage: Uri? = it.data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this@ArticleCreatorActivity.getContentResolver(), selectedImage)
                    pictures[currIndex] = bitmap
                }catch (e: Exception){
                    e.printStackTrace()
                }
                binding.imageView.setImageBitmap(bitmap)
            }
        }
        binding = ActivityArticleCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.plusFloatingActionButton.setOnClickListener {
            makeAlertDialog(steps.size)
        }
        binding.imageView.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setType("image/*")
            galleryResultHandler.launch(photoPickerIntent)
        }
        binding.steps.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                currIndex = steps.indexOf(findViewById(binding.steps.checkedRadioButtonId))
                binding.imageView.setImageBitmap(pictures[currIndex])
                try {
                    binding.editTextText.setText(texts[currIndex])
                } catch (e: Exception){
                    texts.add(currIndex, "")
                }
            }
        })
        binding.editTextText.addTextChangedListener {
            texts[currIndex] = it.toString()
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
                binding.saveFloatingActionButton.visibility =
                    if (binding.root.rootView.height - binding.root.height > 300) View.GONE else View.VISIBLE
                binding.plusFloatingActionButton.visibility =
                    if (binding.root.rootView.height - binding.root.height > 300) View.GONE else View.VISIBLE
        }

        val galleryResultHandlerButton = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            var bitmap: Bitmap? = null
            if(it.resultCode == RESULT_OK) {
                val selectedImage: Uri? = it.data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this@ArticleCreatorActivity.getContentResolver(), selectedImage)
                }catch (e: Exception){
                    e.printStackTrace()
                }
//                    binding1.imageButton.background = BitmapDrawable(resources, bitmap)
                mainPic.value = bitmap!!
            }
        }

        binding.saveFloatingActionButton.setOnClickListener {

            val binding1 = LayoutArticleDataBinding.inflate(layoutInflater)
            if (!add) {
                binding1.editTextTextPersonName.setText(originalArticle.name)
                binding1.imageButton.setText(originalArticle.name)
                mainPic.value = originalArticle.mainPic!!
            }
            val builder = AlertDialog.Builder(this@ArticleCreatorActivity)
            builder.setTitle("Данные о статье")
            builder.setView(binding1.root)
            mainPic.observe(this){
                binding1.imageButton.background = BitmapDrawable(resources, it)
            }
            if (!add)
                binding1.editTextTextPersonName.setText(originalArticle.name)
            binding1.imageButton.setOnClickListener {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.setType("image/*")
                galleryResultHandlerButton.launch(photoPickerIntent)
            }
            builder.setPositiveButton("OK"){dialog, _ ->
                if (steps.size == 0){
                    Toast.makeText(
                        this@ArticleCreatorActivity,
                        "А где?",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                for (num: Int in 0..steps.size - 1){
                    if (steps[num].text.length < 1) {
                        Toast.makeText(
                            this@ArticleCreatorActivity,
                            "Где-то пустое название пункта",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }
                }
                if (pictures.contains(defBmp)){
                    Toast.makeText(
                        this@ArticleCreatorActivity,
                        "Где-то нет картинки",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                if (binding1.imageButton.background == null){
                    Toast.makeText(
                        this@ArticleCreatorActivity,
                        "Нет превью",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                if (binding1.imageButton.text == ""){
                    Toast.makeText(
                        this@ArticleCreatorActivity,
                        "Нет Названия",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                Toast.makeText(
                    this@ArticleCreatorActivity,
                    "Ok",
                    Toast.LENGTH_SHORT
                ).show()
                for (n in 0..texts.size - 1){
                    if (texts[n] == "")
                        texts[n] = " "
                }
                val stepsText = ArrayList<String>()
                for (rb: RadioButton in steps)
                    stepsText.add(rb.text.toString())
                FBConnect().sendArticle(
                    if (add)
                        Article(
                            name = binding1.editTextTextPersonName.text.toString(),
                            mainPic = mainPic.value!!,
                            steps = stepsText,
                            pictures = pictures,
                            texts = texts
                        )
                    else
                        Article(
                            id = originalArticle.id,
                            mainPic = mainPic.value!!,
                            name = originalArticle.name,
                            public = originalArticle.public,
                            texts = texts,
                            pictures = pictures,
                            steps = stepsText
                        )
                )
            }
            builder.setNegativeButton("Cancel"){dialog, _ -> dialog.cancel() }
            binding1.buttApply.setOnClickListener {
                binding1.imageButton.text = binding1.editTextTextPersonName.text
            }
            try {
                builder.show()
            } catch (e: Exception){
                e.printStackTrace()
            }
            /*Log.e("TAG", "onCreate: ${pictures}", )
            for (num: Int in 0..steps.size - 1){
                if (steps[num].text.length < 1) {
                    Toast.makeText(
                        this@ArticleCreatorActivity,
                        "Где-то пустое название пункта",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
            if (pictures.contains(defBmp)){
                Toast.makeText(this@ArticleCreatorActivity, "Где-то нет картинки", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this@ArticleCreatorActivity, "Ok", Toast.LENGTH_SHORT).show()
            val stepsText = ArrayList<String>()
            for (rb: RadioButton in steps)
                stepsText.add(rb.text.toString())
            FBConnect().sendArticle(
                Article(
                    steps = stepsText,
                    pictures = pictures,
                    texts = texts
                )
            )*/
        }
    }

    //AlertDialog for new step
    private fun makeAlertDialog(num: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Название ${num + 1} шага")
        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, _ ->
            if (steps.size > num){
                steps[num].text = input.text.toString()
            }else{
                steps.add(num, RadioButton(this))
                steps[num].text = input.text
                steps[num].layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                binding.steps.addView(steps[num])
                pictures.add(num, defBmp)
                texts.add(num, "")
                steps[num].isChecked = true

                val popupMenu = PopupMenu(this, steps[num])
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId){
                        R.id.rename -> {
                            makeAlertDialog(num)
                            true
                        }
                        R.id.delete -> {

                            val index = steps.indexOf(rbToDel)
                            binding.steps.removeView(steps[index])
                            steps.removeAt(index)
                            pictures.removeAt(index)
                            texts.removeAt(index)
                            if (steps.size > index)
                                steps[index].isChecked = true
                            else
                                if (steps.size > 0)
                                    steps[steps.size-1].isChecked = true
                            true
                        }
                        else -> true
                    }
                }
                steps[num].setOnLongClickListener{
                    rbToDel = it as RadioButton
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
                    true
                }
            }
        }
        builder.setNegativeButton("Cancel"){dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
    private fun initEdit(){
        for (step: Int in 0..originalArticle.steps.size-1){
            val rb = RadioButton(this)
            rb.text = originalArticle.steps[step]
            rb.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            steps.add(step, rb)
            binding.steps.addView(steps[step])
            pictures.add(step, originalArticle.pictures[step])
            texts.add(step, originalArticle.texts[step])
            Log.e("TAG", "initEdit: $texts", )
            binding.editTextText.setText(texts[step])
            binding.imageView.setImageBitmap(pictures[step])
            mainPic.value = originalArticle.mainPic!!
            steps[step].isChecked = true
        }
    }
}