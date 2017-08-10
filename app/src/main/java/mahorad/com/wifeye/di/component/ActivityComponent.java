package mahorad.com.wifeye.di.component;

import dagger.Component;
import mahorad.com.wifeye.di.module.ActivityModule;
import mahorad.com.wifeye.di.scope.PerActivity;
import mahorad.com.wifeye.ui.home.HomeActivity;

@PerActivity
@Component(modules = { ActivityModule.class }, dependencies = { /**/ })
public interface ActivityComponent {

    void inject(HomeActivity activity);

}
