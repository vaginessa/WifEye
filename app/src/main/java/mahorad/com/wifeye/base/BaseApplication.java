package mahorad.com.wifeye.base;

import android.app.Application;

import javax.inject.Inject;

import mahorad.com.wifeye.di.component.ApplicationComponent;
import mahorad.com.wifeye.di.component.DaggerApplicationComponent;
import mahorad.com.wifeye.di.module.ApplicationModule;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApplication extends Application {

    @Inject
    CalligraphyConfig calligraphyConfig;

    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent();
        injectDependencies();
        CalligraphyConfig.initDefault(calligraphyConfig);
        Timber.plant(new Timber.DebugTree());
    }

    private void initializeComponent() {
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void injectDependencies() {
        component.inject(this);
    }

    public static ApplicationComponent component() {
        return component;
    }
}
