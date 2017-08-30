package mahorad.com.wifeye.ui.overview;

import dagger.Module;
import dagger.Provides;
import mahorad.com.wifeye.di.module.ActivityModule;
import mahorad.com.wifeye.di.scope.PerActivity;

/**
 * Created by mahan on 2017-08-29.
 */

@Module(includes = ActivityModule.class)
public class OverviewActivityModule {

    @Provides
    @PerActivity
    OverviewFragment overviewFragment() {
        return new OverviewFragment();
    }

    @Provides
    @PerActivity
    OverviewViewModel overviewViewModel() {
        return new OverviewViewModel();
    }

}
