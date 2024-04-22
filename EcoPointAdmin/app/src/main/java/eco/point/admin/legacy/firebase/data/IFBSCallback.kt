package eco.point.admin.legacy.firebase.data

import eco.point.admin.legacy.elements.Article

interface IFBSCallback {
    fun onResult(articlesList: ArrayList<Article>)
}