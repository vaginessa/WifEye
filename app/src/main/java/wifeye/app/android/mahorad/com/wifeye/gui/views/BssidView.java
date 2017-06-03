package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiSsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class BssidView extends BoxView implements IWifiSsidNameConsumer {

    private static final String HEADER = "H O T  S P O T";

    @Inject
    WifiSsidNamePublisher ssidNamePublisher;

    @Inject
    Utilities utils;

    private String ssid;
    private ImageView stateIcon;

    public BssidView(Context context) {
        super(context);
    }

    public BssidView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BssidView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        finishSignalViewInflation();
    }

    private void finishSignalViewInflation() {
        MainComponent mainComponent =
                MainApplication.mainComponent();
        if (mainComponent != null)
                mainComponent.inject(this);

        if (ssidNamePublisher != null)
            ssidNamePublisher.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (ssidNamePublisher != null)
            ssidNamePublisher.unsubscribe(this);
    }

    private void setupContents() {
        setupStateIconView();
        setContents(stateIcon);
        setFact(IState.Type.Initial.title());
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.no_ssid);
    }

    @Override
    public void refresh() {
        if (ssidNamePublisher == null)
            return;
        ssid = ssidNamePublisher.ssid();
        post(this::updateView);
    }

    @Override
    public void onInternetConnected(String ssid) {
        this.ssid = ssid;
        post(this::updateView);
    }

    @Override
    public void onInternetDisconnected() {
        this.ssid = null;
        post(this::updateView);
    }

    private void updateView() {
        setFact(ssid == null ? "n/a" : ssid);
        String ago = utils.toAgo(
                ssidNamePublisher.date(), getContext());
        setCaption(ago);
        int icon = ssid != null
                ? R.drawable.has_ssid
                : R.drawable.no_ssid;
        stateIcon.setImageResource(icon);
    }

}
