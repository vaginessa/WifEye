package mahorad.com.wifeye.di.component;

import dagger.Component;
import mahorad.com.wifeye.di.module.ServiceModule;
import mahorad.com.wifeye.di.scope.PerService;
import mahorad.com.wifeye.service.EngineService;

@PerService
@Component(modules = { ServiceModule.class }, dependencies = {  })
public interface ServiceComponent {

    void inject(EngineService service);

}
