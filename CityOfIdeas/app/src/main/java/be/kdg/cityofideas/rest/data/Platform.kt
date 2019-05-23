package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Platform (

    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("explanationShort")
    @Expose
    val explanationShort: String?,
    @SerializedName("explanationLong")
    @Expose
    val explanationLong: String?,
/*    @SerializedName("Account")
    @Expose
    val account: Account,*/
    @SerializedName("projects")
    @Expose
    val projects: List<Project>?,

    val logo : String?

    )
