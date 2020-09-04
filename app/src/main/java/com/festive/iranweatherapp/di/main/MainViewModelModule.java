package com.festive.iranweatherapp.di.main;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.festive.iranweatherapp.di.ViewModelKey;
import com.festive.iranweatherapp.ui.main.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    @MainScpoe
    public abstract ViewModel bindsMainViewModel(MainViewModel mainViewModel);

}
