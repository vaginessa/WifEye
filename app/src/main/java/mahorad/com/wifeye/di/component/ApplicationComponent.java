package mahorad.com.wifeye.di.component;

import android.content.Context;

import dagger.Component;
import mahorad.com.wifeye.base.BaseApplication;
import mahorad.com.wifeye.di.module.ApplicationModule;
import mahorad.com.wifeye.di.module.EngineModule;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.scope.PerApplication;
import mahorad.com.wifeye.engine.Engine;

@PerApplication
@Component(modules = { ApplicationModule.class, EngineModule.class }, dependencies = { /**/ })
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Engine engine();

    void inject(BaseApplication app);

    void inject(Engine engine);

}
