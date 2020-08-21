package com.festive.iranweatherapp.di.main;

import com.festive.iranweatherapp.ui.home.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    public abstract MainFragment contributeHomeFragment();
}
