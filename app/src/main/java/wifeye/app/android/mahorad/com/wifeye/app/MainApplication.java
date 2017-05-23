package wifeye.app.android.mahorad.com.wifeye.app;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.ApplicationComponent;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.ApplicationModule;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.DaggerApplicationComponent;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.DaggerMainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainModule;

public class  MainApplication extends Application {

    private static ApplicationComponent appComponent;
    private static MainComponent mainComponent;

    public static ApplicationComponent appComponent() {
        return appComponent;
    }

    public static MainComponent mainComponent() {
        return mainComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setApplicationFont();

        ApplicationModule applicationModule = new ApplicationModule(this);

        appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(applicationModule)
                .build();

        mainComponent = DaggerMainComponent
                .builder()
                .applicationModule(applicationModule)
                .mainModule(new MainModule())
                .build();
    }

    private void setApplicationFont() {
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HelveticaNeue-Light.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
