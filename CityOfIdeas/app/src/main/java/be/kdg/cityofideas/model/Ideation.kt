package be.kdg.cityofideas.model

import java.util.*

data class Ideation(

    val id : Int,
    val title : String,
    val explanation : String,
    var projectId: Int,
    val startDate : GregorianCalendar,
    val endDate : GregorianCalendar,
    val numberOfIdeas : Int,
    val like : Int,
    val comment : Int,
    val logo : Int
)