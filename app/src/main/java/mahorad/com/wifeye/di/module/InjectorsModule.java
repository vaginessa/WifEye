package mahorad.com.wifeye.di.module;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;
import mahorad.com.wifeye.di.scope.PerActivity;
import mahorad.com.wifeye.di.scope.PerService;
import mahorad.com.wifeye.service.EngineService;
import mahorad.com.wifeye.service.EngineServiceModule;
import mahorad.com.wifeye.ui.overview.OverviewActivity;
import mahorad.com.wifeye.ui.overview.OverviewActivityModule;

/**
 * Created by Mahan Rad on 2017-08-22.
 */

@Module(includes = AndroidInjectionModule.class)
public abstract class InjectorsModule {

    @PerActivity
    @ContributesAndroidInjector(modules = OverviewActivityModule.class)
    abstract OverviewActivity overviewActivityInjector();

    @PerService
    @ContributesAndroidInjector(modules = EngineServiceModule.class)
    abstract EngineService engineServiceInjector();

}
