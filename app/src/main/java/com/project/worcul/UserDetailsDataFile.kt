package com.project.worcul;

data class UserDetailsDataFile( val nameOfUserByEmail: String, val EmailOfUser: String) {

    fun getMessage(): String {
        return EmailOfUser
    }

    fun getNameUser(): String{
        return nameOfUserByEmail
    }

}
