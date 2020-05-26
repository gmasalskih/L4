package com.example.l4.api;

import android.util.Log;

import com.example.l4.entity.Repo;
import com.example.l4.entity.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class API {
    private Retrofit builder;
    private static API api;
    public final String BASE_URL = "https://api.github.com";
    private final String TAG = "http";

    private API() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(msg -> Log.i(TAG, msg))
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logger).build();
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build();
    }

    public static API getInstance() {
        if (api == null) api = new API();
        return api;
    }

    public GitHubService getApi() {
        return builder.create(GitHubService.class);
    }

    public interface GitHubService {
        @GET("users/{login}/repos")
        Single<List<Repo>> getReposOfUser(@Path("login") String login);

        @GET("users")
        Single<List<User>> getUsers();
    }

}