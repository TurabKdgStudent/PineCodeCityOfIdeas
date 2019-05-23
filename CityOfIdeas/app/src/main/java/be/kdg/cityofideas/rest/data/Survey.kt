package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Survey (

    @SerializedName("id")
    @Expose
    val id: String? = null,
    @SerializedName("subCommentAccount")
    @Expose
    val surveyAccount: SurveyAccount? = null,
    @SerializedName("title")
    @Expose
    val title: String? = null,
    @SerializedName("about")
    @Expose
    val about: String? = null,
    @SerializedName("questions")
    @Expose
    val questions: List<Question>? = null,
    val numberOfQuestions : String?,
    val phase : String?,
    val phaseLogo : String?

    )
