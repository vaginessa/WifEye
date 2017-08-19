package mahorad.com.wifeye.di.module;

import android.app.Service;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.scope.PerApplication;
import mahorad.com.wifeye.di.scope.PerService;
import mahorad.com.wifeye.engine.Engine;
import mahorad.com.wifeye.publisher.event.persistence.Chronograph;

@Module(includes = {  })
public class ServiceModule {

    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }

    @Provides
    @PerService
    public Chronograph chronograph(@ApplicationContext Context context) {
        return new Chronograph(context);
    }
}
