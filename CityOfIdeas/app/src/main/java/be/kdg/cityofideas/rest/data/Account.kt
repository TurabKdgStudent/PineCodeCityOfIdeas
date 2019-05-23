package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Account (

    @SerializedName("userName")
    @Expose
    val userName: String?,
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("email")
    @Expose
    val email: String?

)
