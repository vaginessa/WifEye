package wifeye.app.android.mahorad.com.wifeye;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import wifeye.app.android.mahorad.com.wifeye.dagger.DaggerMainActivityComponent;
import wifeye.app.android.mahorad.com.wifeye.dagger.MainActivityComponent;
import wifeye.app.android.mahorad.com.wifeye.dagger.MainActivityModule;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.ui.FragmentSummary;
import wifeye.app.android.mahorad.com.wifeye.ui.view.IMainView;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

public class MainActivity extends ActivityManagePermission
        implements OnNavigationItemSelectedListener, IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ColorStateList greens;
    private ColorStateList accent;

    private final MainActivityComponent component =
            DaggerMainActivityComponent
                    .builder()
                    .mainActivityModule(new MainActivityModule(this))
                    .appComponent(MainApplication.appComponent())
                    .build();

    private final Presenter presenter = new Presenter(this);

    private final FragmentSummary summary = new FragmentSummary();

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Inject
    SharedPreferences preferences;
    @Inject
    Utilities utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.onCreate();
        MainApplication.mainComponent().inject(this);

        initUserInterface();
        selectFirstMenuItem();
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

    private void initUserInterface() {
        setupAppToolbarView();
        setupDrawerLayout();
        setupNavigationView();
        setupFloatingButton();
    }

    private void setupAppToolbarView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setupNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_schedule);
    }

    private void setupFloatingButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setSelected(false);
        greens = new ColorStateList(
                new int[][] { new int[0] },
                new int[]   { ResourcesCompat.getColor(getResources(), R.color.colorGreen, null) });
        accent = new ColorStateList(
                new int[][] { new int[0] },
                new int[]   { ResourcesCompat.getColor(getResources(), R.color.colorAccent, null) });
    }

    private void selectFirstMenuItem() {
        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        onNavigationItemSelected(firstItem);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setTitle(item.getTitle());
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_summary:
                putFragment(summary);
                break;
            default:
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void putFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, fragment)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void toggleService(View view) {
        if (utils.isRunning(MainService.class)) {
            presenter.stopMainService();
            showSnackbar("Service stopped.");
        } else {
            presenter.handlePermissions();
            showSnackbar("Service is started.");
        }
    }

    private void showSnackbar(String message) {
        Snackbar
                .make(fab, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void updateServiceState(final boolean enabled, final String date) {
        runOnUiThread(() -> {
            if (enabled)
                setFloatingButtonEnabled();
            else
                setFloatingButtonDisable();
        });
    }

    private void setFloatingButtonEnabled() {
        fab.setImageResource(R.drawable.eye);
        fab.setBackgroundTintList(greens);
    }

    private void setFloatingButtonDisable() {
        fab.setImageResource(R.drawable.eye_off);
        fab.setBackgroundTintList(accent);
    }

    @Override
    public void updateActionState(final Action action, final String date) {
//        runOnUiThread(() -> {
//            actionText.setText(action.toString());
//            actionDate.setText(date);
//        });
    }

    @Override
    public void updatePersistence(String repository) {
//        runOnUiThread(() -> persistence.setText(repository));
    }

    @Override
    public void updateWifiDeviceState(WifiState state) {
//        runOnUiThread(() -> wifiText.setText(state.toString()));
    }

    @Override
    public void toggleMainServiceDisable() {
//        runOnUiThread(() -> service.setChecked(false));
    }

    @Override
    public void updateTowerIdState(final String ctid, final String date) {
//        runOnUiThread(() -> {
//            ctidText.setText(ctid);
//            ctidDate.setText(date);
//        });
    }

    @Override
    public void updateHotspotState(final String ssid, final String date) {
//        runOnUiThread(() -> {
//            ssidText.setText(ssid == null ? "" : ssid);
//            ssidDate.setText(date);
//        });
    }

    @Override
    public void updateEngineState(final String state, String date) {
//        runOnUiThread(() -> {
//            stateText.setText(state);
//            stateDate.setText(date);
//        });
    }

}