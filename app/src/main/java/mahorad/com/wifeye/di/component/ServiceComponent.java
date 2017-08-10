package mahorad.com.wifeye.di.component;

import dagger.Component;
import mahorad.com.wifeye.base.BaseService;
import mahorad.com.wifeye.di.module.ServiceModule;
import mahorad.com.wifeye.di.scope.PerService;

@PerService
@Component(modules = { ServiceModule.class }, dependencies = { /**/ })
public interface ServiceComponent {

    void inject(BaseService service);

}
