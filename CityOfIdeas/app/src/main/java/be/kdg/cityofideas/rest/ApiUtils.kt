package be.kdg.cityofideas.rest

const val VOTE_URL = "https://34.76.13.110/"

class APIUtils {
    fun getAPIService(): ApiService {
        return PostClient().getClient(VOTE_URL)!!.create(ApiService::class.java)
    }

}