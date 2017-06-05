package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IPersistListener;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persist;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class PersistView extends BoxView implements IPersistListener {

    private static final String HEADER = "P E R S I S T";

    @Inject Persist persist;
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

        if (persist != null)
            persist.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (persist != null)
            persist.unsubscribe(this);
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

    private void updateView() {
        String ago = utils.toAgo(
                Persist.date(), getContext());
        setFact(ago);
        setCaption(Persist.data());
    }

}
