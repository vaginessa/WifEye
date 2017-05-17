package wifeye.app.android.mahorad.com.wifeye.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.presenter.IPresenter;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.ui.view.IViewSummary;

public class FragmentSummary extends Fragment implements IViewSummary {

    private final IPresenter presenter = new Presenter(this);

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

        /*************************/
        BoxView c = (BoxView) frame.findViewById(R.id.c);
        c.setHeader("CONNECTION STATE");

        ShimmerTextView stv = new ShimmerTextView(getContext());
        stv.setText("Connecting");
        stv.setTextColor(getResources().getColor(R.color.colorMainBackground));
        stv.setReflectionColor(getResources().getColor(R.color.boxInfoTextColor));
        stv.setTextSize(20);
        stv.setGravity(Gravity.CENTER);
        stv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Shimmer shimmer = new Shimmer();
        shimmer.start(stv);

        c.setContents(stv);
        c.setFact("12:18 Today");
        c.setCaption("Last Occurrence");

        /*************************/
        BoxView d = (BoxView) frame.findViewById(R.id.d);
        d.setHeader("CELL TOWER SIGNAL");
        ImageView hotspot = new ImageView(container.getContext());
        hotspot.setImageResource(R.drawable.hotspots);
        d.setContents(hotspot);
        d.setFact("10:30pm Today");
        d.setCaption("Last Received Date");

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
    public void updateActionState(final Action action, final String date) {
//        runOnUiThread(() -> {
//            actionText.setText(action.toString());
//            actionDate.setText(date);
//        });
    }

    @Override
    public void updateWifiDeviceState(WifiState state) {
//        runOnUiThread(() -> wifiText.setText(state.toString()));
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
