package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    SharedPreferences sharedPrefs();

    @ApplicationContext
    Context context();

    Application application();

}
