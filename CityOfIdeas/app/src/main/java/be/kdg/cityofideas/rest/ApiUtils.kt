package be.kdg.cityofideas.rest
/*Test-Site die ook online staat, dit is ook de laatste versie van onze website maar werd enkel gebruikt bij het developen van de app
 const val VOTE_URL = "https://34.76.13.110/"*/

//Officiele site gedeployed voor de test op 29/05/2019
const val VOTE_URL = "https://34.76.57.106/"

class APIUtils {
    fun getAPIService(): ApiService {
        return PostClient().getClient(VOTE_URL)!!.create(ApiService::class.java)
    }

}