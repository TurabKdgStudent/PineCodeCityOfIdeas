package be.kdg.cityofideas.model

import java.util.*

data class Survey (
    val id : Int,
    val projectId : Int,
    val title : String,
    val startDate : GregorianCalendar,
    val endDate : GregorianCalendar,
    val logo : Int
)