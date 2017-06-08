package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IInternetListener;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IPersistListener;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.persist.Persistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class PersistView extends BoxView implements IPersistListener, IInternetListener {

    private static final String HEADER = "P E R S I S T";

    @Inject IPersistence persistence;
    @Inject Internet internet;
    @Inject Utilities utils;

    private ImageView stateIcon;

    public PersistView(Context context) {
        super(context);
    }

    public PersistView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersistView(Context context, AttributeSet attrs, int defStyle) {
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

        if (persistence != null)
            persistence.subscribe(this);
        if (internet != null)
            internet.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (persistence != null)
            persistence.unsubscribe(this);
        if (internet != null)
            internet.unsubscribe(this);
    }

    private void setupContents() {
        setupStateIconView();
        setContents(stateIcon);
        setFact("-");
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.save);
    }

    @Override
    public void refresh() {
        post(this::updateView);
    }

    @Override
    public void onDataPersisted(String data) {
        post(this::updateView);
    }

    @Override
    public void onInternetConnected(String ssid) {
        post(this::updateView);
    }

    @Override
    public void onInternetDisconnected() {
        post(this::updateView);
    }

    private void updateView() {
        setFact(facts());
        String caption = String.format("%s: %s", Internet.ssid(), Persistence.data());
        setCaption(caption);
    }

    private String facts() {
        String ssid = Internet.ssid();
        if (Utilities.isNullOrEmpty(ssid))
            return "-";
        int towers = persistence
                .towersOf(ssid)
                .size();
        String plural = towers > 1 ? "s" : "";
        String ago = utils.toAgo(
                Persistence.date(), getContext());
        return String.format("%d Tower%s (%s)", towers, plural, ago);
    }
}
