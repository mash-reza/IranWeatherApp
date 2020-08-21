package com.festive.iranweatherapp.di;

import androidx.lifecycle.ViewModelProvider;

import com.festive.iranweatherapp.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindsViewModelProviderFactory(ViewModelProviderFactory factory);
}
