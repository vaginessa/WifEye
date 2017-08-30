package mahorad.com.wifeye.di.component;

import dagger.Component;
import mahorad.com.wifeye.di.module.ServiceModule;
import mahorad.com.wifeye.di.scope.PerService;
import mahorad.com.wifeye.engine.Engine;

/**
 * Created by mahan on 2017-08-30.
 */

@PerService
@Component(modules = ServiceModule.class, dependencies = ApplicationComponent.class)
public interface ServiceComponent {

    void inject(Engine engine);

}
