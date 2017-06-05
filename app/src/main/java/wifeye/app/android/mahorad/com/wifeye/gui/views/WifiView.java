package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiListener;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class WifiView extends BoxView implements IWifiListener {

    private static final String HEADER = "W I F I";

    @Inject Wifi wifi;
    @Inject Utilities utils;

    private ImageView stateIcon;

    public WifiView(Context context) {
        super(context);
    }

    public WifiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WifiView(Context context, AttributeSet attrs, int defStyle) {
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

        if (wifi != null)
            wifi.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (wifi != null)
            wifi.unsubscribe(this);
    }

    private void setupContents() {
        setupStateIconView();
        setContents(stateIcon);
        setFact("-");
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.wifi_off);
    }

    @Override
    public void refresh() {
        post(this::updateView);
    }

    @Override
    public void onWifiStateChanged(Wifi.State state) {
        post(this::updateView);
    }

    private void updateView() {
        setFact(wifi.isEnabled() ? "enabled" : "disabled");
        String ago = utils.toAgo(
                Wifi.date(), getContext());
        setCaption(ago);
        int icon = wifi.isEnabled()
                ? R.drawable.wifi_on
                : R.drawable.wifi_off;
        stateIcon.setImageResource(icon);
    }

}
