package mahorad.com.wifeye.base;

import android.app.Application;

import javax.inject.Inject;

import mahorad.com.wifeye.di.component.ApplicationComponent;
import mahorad.com.wifeye.di.component.DaggerApplicationComponent;
import mahorad.com.wifeye.di.module.ApplicationModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApplication extends Application {

    @Inject
    CalligraphyConfig calligraphyConfig;

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent();
        injectDependencies();
        CalligraphyConfig.initDefault(calligraphyConfig);
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

    public ApplicationComponent component() {
        return component;
    }
}
