package com.docubox.data.modes.local

import java.io.Serializable

// Class to represent a user in our app
data class User(
    val id: String = "", // User Id
    val token: String = "", // Authentication token
    val userEmail: String = "",
    val userName: String = "",
    val rootDirectory: String = ""
) : Serializable
