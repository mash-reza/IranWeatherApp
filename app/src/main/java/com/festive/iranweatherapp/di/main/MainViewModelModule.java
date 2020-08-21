package com.festive.iranweatherapp.di.main;

import androidx.lifecycle.ViewModel;

import com.festive.iranweatherapp.di.ViewModelKey;
import com.festive.iranweatherapp.ui.home.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindsHomeViewModel(MainViewModel mainViewModel);

}
