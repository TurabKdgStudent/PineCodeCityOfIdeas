package be.kdg.cityofideas.model

import java.util.*

data class Project(

    val id : Int,
    val title : String,
    val projectLeader : String,
    val startDate : GregorianCalendar,
    val endDate : GregorianCalendar,
    val logo : Int
)