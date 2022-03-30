package com.project.worcul;

import android.net.Uri
import java.sql.Timestamp
import java.util.jar.Attributes

data class FirebaseMessageDataFile(val nameOfUser: String, val messageOfUser: String, val Timestamp: Long) {

    fun getMessage(): String? {
        return messageOfUser
    }

    fun getNameUser(): String?{
        return nameOfUser
    }

}
