package mahorad.com.wifeye.ui.overview;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.base.BaseActivity;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.service.EngineService;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.R.color.colorGreen;
import static mahorad.com.wifeye.R.color.colorPrimary;
import static mahorad.com.wifeye.R.drawable.eye_off;
import static mahorad.com.wifeye.R.drawable.eye_on;
import static mahorad.com.wifeye.util.Constants.PERMISSIONS;
import static mahorad.com.wifeye.util.Utils.isRunning;
import static mahorad.com.wifeye.util.Utils.openPermissions;

public class OverviewActivity extends BaseActivity {

    @Inject
    OverviewFragment overview;

    @Inject
    OverviewViewModel viewModel;

    @Inject
    CompositeDisposable disposables;

    private ColorStateList greens;
    private ColorStateList accent;

    @BindView(R.id.mainMenu)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.actionButton)
    FloatingActionButton actionButton;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        initUserInterface();
        if (savedInstanceState != null) return;
        selectFirstMenuItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposables.add(engineState());
    }

    private Disposable engineState() {
        return RxEngineServiceMonitor
                .serviceStateChanges()
                .doOnError(Timber::e)
                .observeOn(mainThread())
                .subscribe(this::updateFloatingButton);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initUserInterface() {
        setupAppToolbarView();
        setupDrawerLayout();
        setupNavigationView();
        setupFloatingButton();
    }

    private void setupAppToolbarView() {
        setSupportActionBar(toolbar);
    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle drawerToggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setupNavigationView() {
        navigationView.setCheckedItem(R.id.nav_schedule);
    }

    private void setupFloatingButton() {
        RxView.clicks(actionButton).subscribe(o -> toggleService());
        actionButton.setSelected(false);
        greens = new ColorStateList(
                new int[][] { new int[0] },
                new int[]   { ResourcesCompat.getColor(getResources(), colorGreen, null) });
        accent = new ColorStateList(
                new int[][] { new int[0] },
                new int[]   { ResourcesCompat.getColor(getResources(), colorPrimary, null) });
    }

    public void toggleService() {
        if (isRunning(this, EngineService.class)) {
            stopEngineService();
        } else {
            request(PERMISSIONS);
        }
    }

    @Override
    protected void onPermissionsGranted() {
        startEngineService();
    }

    @Override
    protected void onPermissionsDenied() {
        finishAffinity();
    }

    @Override
    protected void onPermissionsBanned() {
        openPermissions(this);
    }

    public void startEngineService() {
        Intent intent = new Intent(this, EngineService.class);
        startService(intent);
    }

    public void stopEngineService() {
        Intent intent = new Intent(this, EngineService.class);
        stopService(intent);
    }

    public void selectFirstMenuItem() {
        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        onNavigationItemSelected(firstItem);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setTitle(item.getTitle());
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_overview:
                putFragment(overview);
                break;
            default:
        }

        closeDrawer();
        return true;
    }

    private void putFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, fragment)
                .commit();
    }

    public void updateFloatingButton(final boolean enabled) {
        if (enabled) {
            setFloatingButtonEnabled();
            showSnackbar("Service Enabled.");
        } else {
            setFloatingButtonDisable();
            showSnackbar("Service Disabled.");
        }
    }

    private void setFloatingButtonEnabled() {
        actionButton.setImageResource(eye_on);
        actionButton.setBackgroundTintList(greens);
    }

    private void setFloatingButtonDisable() {
        actionButton.setImageResource(eye_off);
        actionButton.setBackgroundTintList(accent);
    }

    public void showSnackbar(String message) {
        Snackbar
                .make(actionButton, message, Snackbar.LENGTH_LONG)
                .setAction("WifiAction", null).show();
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
