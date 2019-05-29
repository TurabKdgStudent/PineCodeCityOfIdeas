package be.kdg.cityofideas.rest

import be.kdg.cityofideas.identity.Login
import be.kdg.cityofideas.identity.Register
import be.kdg.cityofideas.identity.User
import be.kdg.cityofideas.rest.data.Answer
import be.kdg.cityofideas.rest.data.Idea
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.http.POST

/*Test-Site die ook online staat, dit is ook de laatste versie van onze website maar werd enkel gebruikt bij het developen van de app
const val BASE_URL = "https://34.76.13.110/Graphql/Post"
const val IMAGE_URL = "https://34.76.13.110"
*/

//Officiele site gedeployed voor de test op 29/05/2019
const val BASE_URL = "https://34.76.57.106/Graphql/Post"
const val IMAGE_URL = "https://34.76.57.106"

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("IdeaAPI/{id}/like")
    fun ideaVote(@Path("id") pathId : Int,
                 @Header("Authorization")token : String,
                 @Body empty : String)
    : Call<String>

    @Headers("Content-Type: application/json")
    @POST("IdeaAPI/{id}/createIdea")
    fun createIdea(
        @Header("Authorization")token : String,
        @Path("id") pathId: Int,
        @Body idea : Idea
    )
    :Call<String>


    @Headers("Content-Type: application/json")
    @POST("IdeaAPI/{id}/fill")
    fun sendSurvey(
        @Header("Authorization") token: String,
        @Path("id") pathId: Int,
        @Body array: JsonArray?
        )
    :Call<List<Answer>>

    @Headers("Content-Type: application/json")
    @POST("/api/Authentication/GenerateToken")
    fun login(@Body login : Login) : Call<String>

    @GET("secretinfo")
    fun getSecret(@Header("Authorization") authToken : String) : Call<ResponseBody>

    @POST("/api/Authentication/register")
    fun register(@Body register : Register) : Call<User>




}