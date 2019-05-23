package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnswerChoice (

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("text")
    @Expose
    val text: String

    )
