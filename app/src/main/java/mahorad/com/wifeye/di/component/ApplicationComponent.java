package mahorad.com.wifeye.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import mahorad.com.wifeye.base.BaseApplication;
import mahorad.com.wifeye.di.module.ApplicationModule;
import mahorad.com.wifeye.di.module.InjectorsModule;
import mahorad.com.wifeye.engine.Engine;

@Singleton
@Component(modules = { ApplicationModule.class, InjectorsModule.class, })
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }

    void inject(BaseApplication baseApplication);

    void inject(Engine engine);

}
