package com.example.dashboard
import retrofit2.Response
import retrofit2.http.*
interface ApiInterfaceTransport {

    @GET("transports/")
    suspend fun getCountries(): Response<List<String>>
    @GET("/transports/{pays}/")
    suspend fun getTransports(@Path("pays") pays: String?): Response<List<Transports>>
    @POST("transportBookings/")
    suspend fun createBooking(
        @Body transportBooking: TransportBooking
    ):Response<TransportBooking>
    @GET("transportBookings/")
    suspend fun getTransportBookings(): Response<List<TransportBooking>>


    @PUT("transportBookings/{id}")
    suspend fun putPost(
        @Path("id") id : String,
        @Body user: TransportBooking
    ):Response<TransportBooking>
    @DELETE("transportBookings/{id}")
    suspend fun deletePost(
        @Path("id") id : String
    ) : Response<TransportBooking>
}