package wifeye.app.android.mahorad.com.wifeye;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action;
import wifeye.app.android.mahorad.com.wifeye.view.IMainView;

public class MainActivity extends ActivityManagePermission implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Presenter presenter = new Presenter(this);

    private TextView serviceText;
    private TextView stateText;
    private TextView stateDate;
    private TextView ssidText;
    private TextView ssidDate;
    private TextView ctidText;
    private TextView ctidDate;
    private TextView actionText;
    private TextView actionDate;
    private EditText persistence;

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUserInterface();
        presenter.onCreate();
        MainApplication.mainComponent().inject(this);
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

    private void setupUserInterface() {
        serviceText = (TextView) findViewById(R.id.service);
        stateText = (TextView) findViewById(R.id.state);
        stateDate = (TextView) findViewById(R.id.stateDate);
        ssidText = (TextView) findViewById(R.id.ssid);
        ssidDate = (TextView) findViewById(R.id.ssidDate);
        ctidText = (TextView) findViewById(R.id.ctid);
        ctidDate = (TextView) findViewById(R.id.ctidDate);
        actionText = (TextView) findViewById(R.id.action);
        actionDate = (TextView) findViewById(R.id.actionDate);
        persistence = (EditText) findViewById(R.id.persistence);
    }

    public void handlePermissions(View view) {
        presenter.handlePermissions();
    }

    public void startMainService(View view) {
        presenter.startMainService();
    }

    public void stopMainService(View view) {
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


}