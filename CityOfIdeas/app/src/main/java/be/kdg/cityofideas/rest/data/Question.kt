package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Question (

    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("text")
    @Expose
    val text: String?,
    val type: String?,
    @SerializedName("questionChoices")
    @Expose
    val questionChoices: List<QuestionChoice>?
)
/*
    @SerializedName("answers")
    @Expose
    val answers: List<Answer>*/


