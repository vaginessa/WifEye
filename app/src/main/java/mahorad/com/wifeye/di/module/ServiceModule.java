package mahorad.com.wifeye.di.module;

import android.app.Service;

import dagger.Module;

@Module(includes = {/* include modules to fulfill its required dependencies */})
public class ServiceModule {

    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }

}
