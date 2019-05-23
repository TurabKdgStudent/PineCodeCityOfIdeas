package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Maincomment (

    @SerializedName("text")
    @Expose
    val text: String?,
    @SerializedName("date")
    @Expose
    val date: String?,
    @SerializedName("likes")
    @Expose
    val likes: List<Like>?,
    @SerializedName("subcomments")
    @Expose
    val subcomments: List<Subcomment>?

    )
