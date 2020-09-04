package com.festive.iranweatherapp.di;

import com.festive.iranweatherapp.ui.main.MainActivity;
import com.festive.iranweatherapp.di.main.MainFragmentBuilderModule;
import com.festive.iranweatherapp.di.main.MainModule;
import com.festive.iranweatherapp.di.main.MainScpoe;
import com.festive.iranweatherapp.di.main.MainViewModelModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @MainScpoe
    @ContributesAndroidInjector(modules = {MainFragmentBuilderModule.class, MainViewModelModule.class, MainModule.class})
    public abstract MainActivity contributeMainActivity();
}
