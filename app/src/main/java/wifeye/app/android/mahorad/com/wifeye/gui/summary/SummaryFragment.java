package wifeye.app.android.mahorad.com.wifeye.gui.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Date;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp.IPresenter;
import wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp.ISummaryView;
import wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp.SummaryPresenter;
import wifeye.app.android.mahorad.com.wifeye.gui.views.BoxView;

public class SummaryFragment extends Fragment implements ISummaryView {

    private final IPresenter presenter = new SummaryPresenter(this);

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View frame = inflater.inflate(
                R.layout.fragment_summary, container, false);

        /*************************/
        BoxView a = (BoxView) frame.findViewById(R.id.a);
        a.setHeader("LAST ACTION");
        ImageView timer = new ImageView(container.getContext());
        timer.setImageResource(R.drawable.timer);
        a.setContents(timer);
        a.setFact("1,215 cals in");
        a.setCaption("460 cals left");

        /*************************/
        BoxView b = (BoxView) frame.findViewById(R.id.b);
        b.setHeader("APPLICATIONS");
        ImageView info = new ImageView(container.getContext());
        info.setImageResource(R.drawable.info);
        b.setContents(info);
        b.setFact("15,966 steps");
        b.setCaption("1,300 steps to go");

        return frame;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void updateActionState(final Action.Type action, final Date date) {
//        runOnUiThread(() -> {
//            actionText.setText(action.toString());
//            actionDate.setText(date);
//        });
    }

    @Override
    public void updateWifiDeviceState(Wifi.State state) {
//        runOnUiThread(() -> wifiText.setText(state.toString()));
    }

    @Override
    public void updateTowerIdState(final String ctid, final Date date) {
//        runOnUiThread(() -> {
//            ctidText.setText(ctid);
//            ctidDate.setText(date);
//        });
    }

    @Override
    public void updateHotspotState(final String ssid, final Date date) {
//        runOnUiThread(() -> {
//            ssidText.setText(ssid == null ? "" : ssid);
//            ssidDate.setText(date);
//        });
    }

    @Override
    public void updateEngineState(final String state, Date date) {
//        runOnUiThread(() -> {
//            stateText.setText(state);
//            stateDate.setText(date);
//        });
    }

}
