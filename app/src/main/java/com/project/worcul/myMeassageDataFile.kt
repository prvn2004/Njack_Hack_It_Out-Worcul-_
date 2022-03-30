package com.project.worcul;

data class myMessageDataFile(
    val nameOfUser: String? = "",
//    val UserImage: Int,
    val messageOfUser: String? = ""

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


}
