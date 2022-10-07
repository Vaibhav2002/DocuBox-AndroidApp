package com.docubox.data.remote.models.responses


import com.google.gson.annotations.SerializedName

data class StorageConsumption(
    @SerializedName("storageConsumption")
    val storageConsumption: String = "",
    @SerializedName("totalStorage")
    val totalStorage: String = "50"
)