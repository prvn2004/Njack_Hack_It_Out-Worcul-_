package com.project.worcul;

import android.media.Image
import org.w3c.dom.Text
import java.util.jar.Attributes

data class worculDataFile(
    val nameOfUser: String? = "",
//    val UserImage: Int,
    val messageOfUser: String? ="",
    val imageUrl: String? ="",
    val trime: String? =""

) {


    fun getName(): String? {
        return nameOfUser
    }

//    fun getImage(): Int {
//        return UserImage
//    }

    fun getText(): String? {
        return messageOfUser
    }
    fun getLink(): String?{
        return imageUrl

    }
    fun getTime(): String? {
        return trime

    }


}
