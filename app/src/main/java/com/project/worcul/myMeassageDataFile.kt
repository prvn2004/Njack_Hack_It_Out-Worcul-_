package com.project.worcul;

data class myMessageDataFile(
    val nameOfUser: String? = "",
//    val UserImage: Int,
    val messageOfUser: String? = "",
    val imageUrl: String? = "",
    val trime: String? = ""

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
