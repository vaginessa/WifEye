package wifeye.app.android.mahorad.com.wifeye.gui.mvp;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.gui.overview.OverviewFragment;

import static wifeye.app.android.mahorad.com.wifeye.R.color.colorAccent;
import static wifeye.app.android.mahorad.com.wifeye.R.color.colorGreen;

public class HomeView extends FrameLayout {

    private final ActivityManagePermission activity;
    private final OverviewFragment overview = new OverviewFragment();

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

    /**
     *
     * @param activity
     */
    public HomeView(Activity activity) {
        super(activity);
        this.activity = (ActivityManagePermission) activity;
        inflate(getContext(), R.layout.activity_main, this);
        ButterKnife.bind(this);
        initUserInterface();
    }

    private void initUserInterface() {
        setupAppToolbarView();
        setupDrawerLayout();
        setupNavigationView();
        setupFloatingButton();
    }

    private void setupAppToolbarView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.setSupportActionBar(toolbar);
    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle drawerToggle =
                new ActionBarDrawerToggle((Activity) getContext(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setupNavigationView() {
        navigationView.setCheckedItem(R.id.nav_schedule);
    }

    private void setupFloatingButton() {
        actionButton = (FloatingActionButton) findViewById(R.id.actionButton);
        actionButton.setSelected(false);
        greens = new ColorStateList(
                new int[][] { new int[0] },
                new int[]   { ResourcesCompat.getColor(getResources(), colorGreen, null) });
        accent = new ColorStateList(
                new int[][] { new int[0] },
                new int[]   { ResourcesCompat.getColor(getResources(), colorAccent, null) });
    }

    public void selectFirstMenuItem() {
        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        onNavigationItemSelected(firstItem);
    }

    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        activity.setTitle(item.getTitle());
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
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, fragment)
                .commit();
    }

    public void updateFloatingButton(final boolean enabled) {
        activity.runOnUiThread(() -> {
            if (enabled) {
                setFloatingButtonEnabled();
            } else {
                setFloatingButtonDisable();
                showSnackbar("Service Disabled.");
            }
        });
    }

    private void setFloatingButtonEnabled() {
        actionButton.setImageResource(R.drawable.eye);
        actionButton.setBackgroundTintList(greens);
    }

    private void setFloatingButtonDisable() {
        actionButton.setImageResource(R.drawable.eye_off);
        actionButton.setBackgroundTintList(accent);
    }

    public void showSnackbar(String message) {
        Snackbar
                .make(actionButton, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public Observable<Object> observeActionButton() {
        return RxView.clicks(actionButton);
    }

    public Observable<MenuItem> observeNavigationView() {
        return RxNavigationView.itemSelections(navigationView);
    }
}
