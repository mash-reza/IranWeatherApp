package com.festive.iranweatherapp.di.main;

import android.app.Application;
import android.content.Context;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.festive.iranweatherapp.R;
import com.festive.iranweatherapp.repository.main.database.MainDao;
import com.festive.iranweatherapp.repository.main.database.MainDatabase;
import com.festive.iranweatherapp.repository.main.network.MainApi;
import com.festive.iranweatherapp.ui.main.MainActivity;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScpoe
    @Provides
    public MainApi providesMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }

    @MainScpoe
    @Provides
    public MainDao provideMainDao(MainDatabase mainDatabase) {
        return mainDatabase.dao();
    }

    @MainScpoe
    @Provides
    public NavController provideNavController(MainActivity mainActivity) {
        return Navigation.findNavController(mainActivity, R.id.nav_host_fragment);
    }

    @MainScpoe
    @Provides
    public RequestManager provideGlide(Application application){
        return Glide.with(application);
    }
}
