package mahorad.com.wifeye.base;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import mahorad.com.wifeye.di.component.ApplicationComponent;
import mahorad.com.wifeye.di.component.DaggerApplicationComponent;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

public class BaseApplication extends Application
        implements HasActivityInjector, HasServiceInjector {

    private static ApplicationComponent component;

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    DispatchingAndroidInjector<Service> serviceInjector;

    @Inject
    CalligraphyConfig calligraphyConfig;


    @Override
    public void onCreate() {
        super.onCreate();
        buildApplicationComponent();
        component.inject(this);
        CalligraphyConfig.initDefault(calligraphyConfig);
        Timber.plant(new Timber.DebugTree());
    }

    private void buildApplicationComponent() {
        component = DaggerApplicationComponent
                .builder()
                .application(this)
                .build();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceInjector;
    }

    public static ApplicationComponent component() {
        return component;
    }

}
