package mahorad.com.wifeye.di.component;

import android.content.Context;

import dagger.Component;
import mahorad.com.wifeye.base.BaseActivity;
import mahorad.com.wifeye.di.module.ActivityModule;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.scope.PerActivity;
import mahorad.com.wifeye.di.scope.PerApplication;
import mahorad.com.wifeye.ui.overview.OverviewActivity;

@PerActivity
@Component(modules = { ActivityModule.class }, dependencies = { ApplicationComponent.class })
public interface ActivityComponent {

    void inject(OverviewActivity activity);

}
