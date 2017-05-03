package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Component;

@ApplicationScope
@Component(modules = AppModule.class)
public interface AppComponent {

    SharedPreferences sharedPrefs();

    Context context();

    Application application();

}
