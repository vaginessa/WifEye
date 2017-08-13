package mahorad.com.wifeye.di.component;

import android.content.Context;

import dagger.Component;
import mahorad.com.wifeye.base.BaseApplication;
import mahorad.com.wifeye.di.module.ApplicationModule;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.scope.PerApplication;

@PerApplication
@Component(modules = { ApplicationModule.class }, dependencies = { /**/ })
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    void inject(BaseApplication app);

}
