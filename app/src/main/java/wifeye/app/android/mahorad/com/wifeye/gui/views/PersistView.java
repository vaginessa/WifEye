package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.events.InternetEvent;
import wifeye.app.android.mahorad.com.wifeye.app.events.PersistenceEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persistence;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class PersistView extends AbstractBoxView {

    public static final String TAG = PersistView.class.getSimpleName();

    private static final String HEADER = "P E R S I S T";

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
    protected void inject() {
        mainComponent.inject(this);
    }

    @Override
    public void attachViewDisposables() {
        Disposable persistDisposable = Persistence
                .observable()
                .subscribe(e -> post(() -> updateCaption(e)));
        attachDisposable(persistDisposable);

        Disposable internetDisposable = Internet
                .observable(getContext())
                .subscribe(e -> post(() -> updateFacts(e)));
        attachDisposable(internetDisposable);
    }

    @Override
    public void reset() {
        setHeader(HEADER);
        setupStateIconView();
        setContents(stateIcon);
        setFact("-");
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.save);
    }

    private void updateFacts(InternetEvent e) {
        synchronized (this) {
            String fact = "-";
            if (e.connected()) {
                int towers = Persistence
                        .towersOf(e.ssid())
                        .size();
                fact = String.format("%d-towers (%s)", towers, e.ssid());
            }
            setFact(fact);
        }
    }

    private void updateCaption(PersistenceEvent e) {
        synchronized (this) {
            Log.d(TAG, "persist: " + e.ssid() + " " + e.ctid());
            setCaption(e.ctid());
        }
    }

}
