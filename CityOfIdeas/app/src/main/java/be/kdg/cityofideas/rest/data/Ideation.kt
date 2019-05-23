package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ideation (

    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("question")
    @Expose
    val question: String?,
    val about : String?,
    val picture : String?,
    @SerializedName("likes")
    @Expose
    val likes: List<Like>?,
    @SerializedName("ideas")
    @Expose
    val ideas: List<Idea>?,
    val numberOfLikes : String?,
    val numberOfIdeas : String?,
    val numberOfShares : String?

    )
