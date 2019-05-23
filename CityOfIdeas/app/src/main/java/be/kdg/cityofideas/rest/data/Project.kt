package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Project (

    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("about")
    @Expose
    val about: String?,
    @SerializedName("picture")
    @Expose
    val picture : String?,
    @SerializedName("isActive")
    @Expose
    val status : String?,
    @SerializedName("phases")
    @Expose
    val phases: List<Phase>?

    )
