package wifeye.app.android.mahorad.com.wifeye;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import wifeye.app.android.mahorad.com.wifeye.dagger.*;

public class MainApplication extends Application {

    private static AppComponent appComponent;
    private static MainComponent mainComponent;

    public static AppComponent appComponent() {
        return appComponent;
    }

    public static MainComponent mainComponent() {
        return mainComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setApplicationFont();

        AppModule applicationModule = new AppModule(this);

        appComponent = DaggerAppComponent
                .builder()
                .appModule(applicationModule)
                .build();

        mainComponent = DaggerMainComponent
                .builder()
                .appModule(applicationModule)
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
