package com.festive.iranweatherapp.di.main;

import com.festive.iranweatherapp.di.main.choose.ChooseModule;
import com.festive.iranweatherapp.di.main.choose.ChooseScope;
import com.festive.iranweatherapp.di.main.choose.ChooseViewModelModule;
import com.festive.iranweatherapp.di.main.home.HomeModule;
import com.festive.iranweatherapp.di.main.home.HomeScope;
import com.festive.iranweatherapp.di.main.home.HomeViewModelModule;
import com.festive.iranweatherapp.ui.main.choose.ChooseFragment;
import com.festive.iranweatherapp.ui.main.choose.ChooseViewModel;
import com.festive.iranweatherapp.ui.main.home.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @HomeScope
    @ContributesAndroidInjector(modules = {HomeModule.class, HomeViewModelModule.class})
    public abstract HomeFragment contributeHomeFragment();

    @ChooseScope
    @ContributesAndroidInjector(modules = {ChooseModule.class, ChooseViewModelModule.class})
    public abstract ChooseFragment contributeChooseFragment();
}
