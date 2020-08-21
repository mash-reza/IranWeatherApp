package com.festive.iranweatherapp.di;

import com.festive.iranweatherapp.MainActivity;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ActivityBuildersModule {

    @Binds
    public abstract MainActivity bindsMainActivity(MainActivity mainActivity);
}
