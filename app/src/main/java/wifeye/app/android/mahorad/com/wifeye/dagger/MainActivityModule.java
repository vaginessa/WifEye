package wifeye.app.android.mahorad.com.wifeye.dagger;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.MainActivity;
import wifeye.app.android.mahorad.com.wifeye.dagger.annotations.MainActivityScope;

@Module
public class MainActivityModule {

    private final MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @MainActivityScope
    public String provideSomething() {
        return "";
    }
}
