package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.dagger.annotations.ApplicationScope;

@ApplicationScope
@Component(modules = AppModule.class)
public interface AppComponent {

    SharedPreferences sharedPrefs();

    @ApplicationContext
    Context context();

    Application application();

}
