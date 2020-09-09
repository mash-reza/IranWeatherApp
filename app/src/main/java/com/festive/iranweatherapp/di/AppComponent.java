package com.festive.iranweatherapp.di;

import android.app.Application;

import com.festive.iranweatherapp.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuildersModule.class,
        ViewModelFactoryModule.class})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
