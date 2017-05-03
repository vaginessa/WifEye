package wifeye.app.android.mahorad.com.wifeye;

import android.app.Application;

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
}
