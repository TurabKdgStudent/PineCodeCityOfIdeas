package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubCommentAccount (

    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("userName")
    @Expose
    val userName: String,
    @SerializedName("email")
    @Expose
    val email: String

    )
