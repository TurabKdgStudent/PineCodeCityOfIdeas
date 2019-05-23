package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Idea (

    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("date")
    @Expose
    val date: String?,
    @SerializedName("explanationShort")
    @Expose
    val explanationShort: String?,
    @SerializedName("explanationLong")
    @Expose
    val explanationLong: String?,
    @SerializedName("photo")
    @Expose
    val photo: String?,
    @SerializedName("account")
    @Expose
    val account: Account?,
    val userName: String?,
    @SerializedName("likes")
    @Expose
    val likes: List<Like>?,
    @SerializedName("maincomments")
    @Expose
    val maincomments: List<Maincomment>?,
    val numberOfLikes : String?,
    val numberOfComments : String?,
    val numberOfShares : String?

    )
