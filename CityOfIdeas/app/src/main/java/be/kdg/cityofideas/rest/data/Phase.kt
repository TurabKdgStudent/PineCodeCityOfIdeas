package be.kdg.cityofideas.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Phase (

    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("about")
    @Expose
    val about: String?,
    @SerializedName("startDate")
    @Expose
    val startDate: String?,
    @SerializedName("endDate")
    @Expose
    val endDate: String?,
    @SerializedName("logo")
    @Expose
    val logo: Int?,
    @SerializedName("ideations")
    @Expose
    val ideations: List<Ideation>?,
    @SerializedName("surveys")
    @Expose
    val surveys: List<Survey>?

    )
