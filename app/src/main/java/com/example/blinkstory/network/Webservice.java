package com.example.blinkstory.network;

import com.example.blinkstory.Constant;
import com.example.blinkstory.model.entity.Category;
import com.example.blinkstory.model.entity.CategoryWrapper;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.model.entity.ElementWrapper;
import com.example.blinkstory.network.model.Paging;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface Webservice {

    @POST(Constant.GET_LIST_CATEGORY_URL)
    Observable<List<Category>> categories();

    @GET(Constant.URL_ADD_DATA)
    Observable<CategoryWrapper> addElement(@Body Element element);

    @POST(Constant.GET_LIST_ELEMENT_URL)
    Observable<ElementWrapper> elements(@Body Paging value);

    @POST(Constant.URL_UPLOAD_DATA)
    Observable<CategoryWrapper> uploadFile(@Body File file);

    @GET("3/movie/{movieId}/videos")
    Observable<CategoryWrapper> trailers(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Observable<CategoryWrapper> reviews(@Path("movieId") String movieId);

    @GET("3/search/movie?language=en-US&page=1")
    Observable<CategoryWrapper> searchMovies(@Query("query") String searchQuery);

}
