package com.docubox.data.modes.remote.responses


import com.google.gson.annotations.SerializedName

data class StorageConsumption(
    @SerializedName("storageConsumption")
    val storageConsumption: String = ""
)