package mahorad.com.wifeye.di.module;

import android.app.Service;

import dagger.Module;
import dagger.Provides;
import mahorad.com.wifeye.di.scope.PerApplication;
import mahorad.com.wifeye.di.scope.PerService;
import mahorad.com.wifeye.engine.Engine;

@Module(includes = {  })
public class ServiceModule {

    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }


}
