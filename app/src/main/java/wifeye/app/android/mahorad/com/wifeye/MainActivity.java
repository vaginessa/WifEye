package wifeye.app.android.mahorad.com.wifeye;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import javax.inject.Inject;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.ui.FragmentSummary;
import wifeye.app.android.mahorad.com.wifeye.ui.view.IMainView;

public class MainActivity extends ActivityManagePermission
        implements OnNavigationItemSelectedListener, IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Presenter presenter = new Presenter(this);

    private final FragmentSummary summary = new FragmentSummary();
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FloatingActionButton floatingButton;

    private TextView serviceText;
    private TextView wifiText;
    private TextView stateText;
    private TextView stateDate;
    private TextView ssidText;
    private TextView ssidDate;
    private TextView ctidText;
    private TextView ctidDate;
    private TextView actionText;
    private TextView actionDate;
    private EditText persistence;
    private ToggleButton service;

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUserInterface();
        presenter.onCreate();
        MainApplication.mainComponent().inject(this);

        setupToolbarView();
        setupDrawerLayout();
        setupNavigationView();
        setupFloatingButton();
        selectFirstMenuItem();
    }

    private void selectFirstMenuItem() {
        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        onNavigationItemSelected(firstItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        presenter.onResume();
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

    private void setupUserInterface() {
//        serviceText = (TextView) findViewById(R.id.service);
//        stateText = (TextView) findViewById(R.id.state);
//        stateDate = (TextView) findViewById(R.id.stateDate);
//        ssidText = (TextView) findViewById(R.id.ssid);
//        ssidDate = (TextView) findViewById(R.id.ssidDate);
//        ctidText = (TextView) findViewById(R.id.ctid);
//        ctidDate = (TextView) findViewById(R.id.ctidDate);
//        actionText = (TextView) findViewById(R.id.action);
//        actionDate = (TextView) findViewById(R.id.actionDate);
//        wifiText = (TextView) findViewById(R.id.wifi);
//        persistence = (EditText) findViewById(R.id.persistence);
//        service = (ToggleButton) findViewById(R.id.serviceButton);
    }

    public void toggleService(View view) {
        ToggleButton b = (ToggleButton) view;
        if (b.isChecked()) {
            presenter.handlePermissions();
        } else
            presenter.stopMainService();
    }


    @Override
    public void updateServiceState(final boolean enabled, final String date) {
        runOnUiThread(() -> serviceText.setText(enabled ? "ENABLE" : "DISABLE"));
    }

    @Override
    public void updateActionState(final Action action, final String date) {
        runOnUiThread(() -> {
            actionText.setText(action.toString());
            actionDate.setText(date);
        });
    }

    @Override
    public void updatePersistence(String repository) {
        runOnUiThread(() -> persistence.setText(repository));
    }

    @Override
    public void updateWifiDeviceState(WifiState state) {
        runOnUiThread(() -> wifiText.setText(state.toString()));
    }

    @Override
    public void toggleMainServiceDisable() {
        runOnUiThread(() -> service.setChecked(false));
    }

    @Override
    public void updateTowerIdState(final String ctid, final String date) {
        runOnUiThread(() -> {
            ctidText.setText(ctid);
            ctidDate.setText(date);
        });
    }

    @Override
    public void updateHotspotState(final String ssid, final String date) {
        runOnUiThread(() -> {
            ssidText.setText(ssid == null ? "" : ssid);
            ssidDate.setText(date);
        });
    }

    @Override
    public void updateEngineState(final String state, String date) {
        runOnUiThread(() -> {
            stateText.setText(state);
            stateDate.setText(date);
        });
    }










    private void setupToolbarView() {
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
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(view -> Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
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
}