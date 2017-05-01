package wifeye.app.android.mahorad.com.wifeye;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.state.IState;
import wifeye.app.android.mahorad.com.wifeye.view.IMainView;

public class MainActivity extends ActivityManagePermission implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Presenter presenter = new Presenter(this);

    private TextView serviceState;
    private TextView deviceState;
    private TextView ssidTextView;
    private TextView lastConnect;
    private TextView actionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUserInterface();
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

    private void setupUserInterface() {
        serviceState = (TextView) findViewById(R.id.serviceState);
        deviceState = (TextView) findViewById(R.id.deviceState);
        ssidTextView = (TextView) findViewById(R.id.ssid);
        lastConnect = (TextView) findViewById(R.id.lastConnect);
        actionText = (TextView) findViewById(R.id.action);
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
    public void updateServiceState(final boolean enabled) {
        runOnUiThread(() -> serviceState.setText(enabled ? "ENABLE" : "DISABLE"));
    }

    @Override
    public void updateOngoingAction(String event) {
        runOnUiThread(() -> actionText.setText(event));
    }

    @Override
    public void updateSsidNameInfo(final String ssid) {
        runOnUiThread(() -> {
            ssidTextView.setText(ssid == null ? "" : ssid);
            lastConnect.setText(ssid == null ? "" : getDate());
        });
    }

    private String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }

    @Override
    public void updateEngineState(final IState state) {
        runOnUiThread(() -> deviceState.setText(state.toString()));
    }

}