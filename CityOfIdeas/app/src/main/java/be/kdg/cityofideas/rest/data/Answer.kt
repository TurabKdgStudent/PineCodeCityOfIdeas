package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Answer (

    @SerializedName("questionId")
    @Expose
    val id: Int?,
    @SerializedName("subCommentAccount")
    @Expose
    val account: Account?,
    @SerializedName("text")
    @Expose
    val text: String?,
    @SerializedName("answerChoice")
    @Expose
    val answerChoice: AnswerChoice?

    )
