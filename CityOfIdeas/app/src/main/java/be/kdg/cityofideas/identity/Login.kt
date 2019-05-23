package be.kdg.cityofideas.identity

class Login (
    private var Email : String,
    private var Password : String){
    private lateinit var currentUser : User


fun createUser(id : String?,user: String,token: String){
    currentUser = User(id,user,token)
}

fun getUser() : User {
    return currentUser
}
}
