package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Subcomment (

    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("subCommentAccount")
    @Expose
    val subCommentAccount: SubCommentAccount,
    @SerializedName("likes")
    @Expose
    val likes: List<Like>

    )
