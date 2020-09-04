package com.festive.iranweatherapp.di.main.choose;

import androidx.lifecycle.ViewModel;

import com.festive.iranweatherapp.di.ViewModelKey;
import com.festive.iranweatherapp.ui.main.choose.ChooseViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ChooseViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChooseViewModel.class)
    @ChooseScope
    public abstract ViewModel bindsChooseViewModel(ChooseViewModel chooseViewModel);
}
