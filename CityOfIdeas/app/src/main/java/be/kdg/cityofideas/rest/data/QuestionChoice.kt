package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionChoice (

    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("text")
    @Expose
    val text: String?,
    val questionType: String?
)




