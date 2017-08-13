package mahorad.com.wifeye.base;

import android.app.Service;

import mahorad.com.wifeye.di.component.ApplicationComponent;
import mahorad.com.wifeye.di.component.DaggerServiceComponent;
import mahorad.com.wifeye.di.component.ServiceComponent;
import mahorad.com.wifeye.di.module.ServiceModule;

public abstract class BaseService extends Service {

    private ServiceComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent();
        injectDependencies();
    }

    protected void initializeComponent() {
        ApplicationComponent applicationComponent =
                ((BaseApplication) getApplication()).component();
        component = DaggerServiceComponent
                .builder()
                .applicationComponent(applicationComponent)
                .serviceModule(new ServiceModule(this))
                .build();
    }

    public ServiceComponent component() {
        return component;
    }

    protected abstract void injectDependencies();

}
