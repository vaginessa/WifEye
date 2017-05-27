package wifeye.app.android.mahorad.com.wifeye.gui;

import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.gui.dagger.DaggerHomeComponent;
import wifeye.app.android.mahorad.com.wifeye.gui.dagger.HomeComponent;
import wifeye.app.android.mahorad.com.wifeye.gui.dagger.HomeModule;
import wifeye.app.android.mahorad.com.wifeye.gui.mvp.HomePresenter;
import wifeye.app.android.mahorad.com.wifeye.gui.mvp.HomeView;

public class HomeActivity extends ActivityManagePermission
{
    private static final String TAG = HomeActivity.class.getSimpleName();

    private final HomeComponent component =
            DaggerHomeComponent
                    .builder()
                    .homeModule(new HomeModule(this))
                    .mainComponent(
                            MainApplication.mainComponent()
                    )
                    .build();
    @Inject
    HomeView view;

    @Inject
    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setHomeComponent(component);
        component.inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (view.isDrawerOpen()) {
            view.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}