package eco.point.admin.legacy.enums

import java.lang.StringBuilder

enum class DataType(val data: String) {
    POINTS("Points"),
    USERS("Users"),
    BUGS("user_alarma"),
    WRONG_POINTS("RSO_point_alarma"),
    USERS_QUESTIONS("questions"),
    BIG_POINTS("Big"),
    SMALL_POINTS("Small"),
    NEW_POINTS("new_RSO_point"),
    ARTICLES("Articles"),
    ORGANIZATIONS("Organizations")
}

    /*private val key: String? = null
    open fun DataType(key: String?) {
        this.key = key
    }*/

    /*open fun getKey(): String? {
        return key
    }

    open operator fun get(s: String): DataType? {
        if (s == USERS.getKey()) return USERS
        if (s == BUGS.getKey()) return BUGS
        if (s == WRONG_POINTS.getKey()) return WRONG_POINTS
        if (s == USERS_QUESTIONS.getKey()) return USERS_QUESTIONS
        if (s == BIG_POINTS.getKey()) return BIG_POINTS
        if (s == SMALL_POINTS.getKey()) return SMALL_POINTS
        return if (s == NEW_POINTS.getKey()) NEW_POINTS else null
    }*/