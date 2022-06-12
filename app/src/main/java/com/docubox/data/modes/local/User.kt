package com.docubox.data.modes.local

import java.io.Serializable

data class User(
    val id: String = "",
    val token: String = "",
    val userEmail: String = "",
    val userName: String = ""
) : Serializable
