package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    Application application() {
        return application;
    }

    @Provides
    @ApplicationScope
    Context context(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    SharedPreferences sharedPreferences(Application application) {
        return PreferenceManager
                .getDefaultSharedPreferences(application);
    }
}
