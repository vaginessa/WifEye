package wifeye.app.android.mahorad.com.wifeye;

import android.app.Application;

import wifeye.app.android.mahorad.com.wifeye.dagger.*;

public class MainApplication extends Application {

    private static MainComponent mainComponent;

    public static MainComponent mainComponent() {
        return mainComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainComponent = DaggerMainComponent
                .builder()
                .contextModule(new ContextModule(this))
                .mainModule(new MainModule())
                .build();
    }
}
