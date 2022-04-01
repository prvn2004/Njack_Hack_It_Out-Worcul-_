package com.project.worcul

import android.icu.text.CaseMap

data class ArticleDataFile(
    val TitleofPost: String? = "",
    val ContentofPost: String? = "",
    val urlOfPost: String? = "",
    val urlofImage: String? = "",
    val sourceofPost: String? = "",
    val authorofPost: String? = "",
    val timeofPost: String? = ""
) {

    fun getTime(): String? {
        return timeofPost
    }

    fun getTitle(): String? {
        return TitleofPost
    }

    fun getContent(): String? {
        return ContentofPost
    }

    fun geturlOfPost(): String? {
        return urlOfPost
    }

    fun geturlOfImage(): String? {
        return urlofImage
    }

    fun getsourceName(): String? {
        return sourceofPost
    }

    fun getauthorofPost(): String? {
        return authorofPost
    }


}