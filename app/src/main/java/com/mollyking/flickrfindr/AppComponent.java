package com.mollyking.flickrfindr;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

@Component(modules = {ApiModule.class,
                    AndroidSupportInjectionModule.class,
                    ActivityModule.class,
                    ViewModelModule.class
})

@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application app);

        AppComponent build();
    }

    void inject(FlickrApp app);
}
