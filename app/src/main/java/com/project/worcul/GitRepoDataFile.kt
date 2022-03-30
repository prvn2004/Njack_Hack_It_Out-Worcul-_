package com.project.worcul;

data class GitRepoDataFile(
    val nameOfRepo: String? = "",
    val descriptionOfRepo: String? = "",
    val urlOfRepo: String? = "",
    ) {


    fun getName(): String? {
        return nameOfRepo
    }

    fun getUrl(): String? {
        return urlOfRepo
    }

    fun getdescription(): String? {
        return descriptionOfRepo
    }


}
