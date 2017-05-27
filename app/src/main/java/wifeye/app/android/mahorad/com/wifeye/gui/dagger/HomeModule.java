package wifeye.app.android.mahorad.com.wifeye.gui.dagger;

import dagger.Module;
import dagger.Provides;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import wifeye.app.android.mahorad.com.wifeye.gui.mvp.HomePresenter;
import wifeye.app.android.mahorad.com.wifeye.gui.mvp.HomeView;

@Module
public class HomeModule {

    private final ActivityManagePermission activity;

    public HomeModule(ActivityManagePermission activity) {
        this.activity = activity;
    }

    @Provides
    @HomeScope
    public ActivityManagePermission activity() {
        return activity;
    }

    @Provides
    @HomeScope
    public HomeView homeView() {
        return new HomeView(activity);
    }

    @Provides
    @HomeScope
    public HomePresenter homePresenter() {
        return new HomePresenter();
    }

 }
