package com.example.dashboard
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
        @GET("hotels/")
        suspend fun getCountries(): Response<List<String>>
        @GET("/hotels/{pays}/")
        suspend fun getHotels(@Path("pays") pays: String?): Response<List<Hotels>>
        @POST("hotelBookings/")
        suspend fun createBooking(
                @Body hotelBooking: HotelBooking
        ):Response<HotelBooking>
        @GET("hotelBookings/")
        suspend fun getHotelBookings(): Response<List<HotelBooking>>


        @PUT("hotelBookings/{id}")
        suspend fun putPost(
                @Path("id") id : String,
                @Body user: HotelBooking
        ):Response<HotelBooking>
        @DELETE("hotelBookings/{id}")
        suspend fun deletePost(
                @Path("id") id : String
        ) : Response<HotelBooking>
        @POST("signup/")
        suspend fun signup(
                @Body user: User
        ):Response<User>
        @POST("signin/")
        suspend fun signin(
                @Body user: User
        ):Response<User>
}