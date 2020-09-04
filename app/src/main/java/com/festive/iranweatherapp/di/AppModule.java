package com.festive.iranweatherapp.di;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.festive.iranweatherapp.repository.main.database.MainDatabase;
import com.festive.iranweatherapp.repository.main.network.LiveDataCallAdapterFactory;
import com.festive.iranweatherapp.repository.main.network.NetworkConnectionInterceptor;
import com.festive.iranweatherapp.repository.main.network.RxErrorHandlingCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class AppModule {

    @Singleton
    @Provides
    public static Retrofit provideRetrofit(Application application) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(new OkHttpClient()
                        .newBuilder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(new NetworkConnectionInterceptor(application))
                        .build())
                .build();
    }

    @Singleton
    @Provides
    static MainDatabase provideRoom(Application application) {
        return Room.databaseBuilder(application, MainDatabase.class, "db")
                .createFromAsset("weather.db")
                .build();
    }
}
