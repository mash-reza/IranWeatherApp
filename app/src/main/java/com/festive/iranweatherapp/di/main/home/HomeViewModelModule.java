package com.festive.iranweatherapp.di.main.home;

import androidx.lifecycle.ViewModel;

import com.festive.iranweatherapp.di.ViewModelKey;
import com.festive.iranweatherapp.ui.main.home.HomeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    @HomeScope
    public abstract ViewModel bindsHomeViewModel(HomeViewModel homeViewModel);
}
