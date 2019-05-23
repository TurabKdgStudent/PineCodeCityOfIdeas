package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Like (

    @SerializedName("id")
    @Expose
    val id: Int

    )
