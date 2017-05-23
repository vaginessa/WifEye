package wifeye.app.android.mahorad.com.wifeye.gui.dagger;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.gui.MainActivity;

@Module
public class MainActivityModule {

    private final MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @MainActivityScope
    public MainActivity mainActivity() {
        return activity;
    }


 }
