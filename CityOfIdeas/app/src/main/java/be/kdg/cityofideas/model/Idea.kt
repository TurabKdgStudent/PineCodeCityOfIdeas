package be.kdg.cityofideas.model

data class Idea(
    val id : Int,
    val title : String,
    val explanation : String,
    var IdeationId: Int,
    val like : Int,
    val comment : Int,
    val logo : Int
)