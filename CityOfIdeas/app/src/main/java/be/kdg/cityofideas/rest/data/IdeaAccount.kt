package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IdeaAccount (

    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("userName")
    @Expose
    val userName: String

    )
