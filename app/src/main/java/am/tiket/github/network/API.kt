package am.tiket.github.network

import am.tiket.github.model.Responses
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET(Networks.userList)
    fun getUserList(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ) : Observable<Responses>

}