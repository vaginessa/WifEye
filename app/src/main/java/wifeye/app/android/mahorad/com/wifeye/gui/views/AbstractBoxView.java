package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;

public abstract class AbstractBoxView extends BoxView {

    private CompositeDisposable boxViewDisposables = new CompositeDisposable();
    private Disposable serviceDisposable;

    @NonNull
    protected final MainComponent mainComponent =
            MainApplication.mainComponent();

    public AbstractBoxView(Context context) {
        super(context);
    }

    public AbstractBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inject();
        serviceDisposable = MainService
                .observable()
                .subscribe(this::onServiceEvent);
        reset();
    }

    protected abstract void inject();

    private void onServiceEvent(boolean e) {
        if (e)
            attachViewDisposables();
        else {
            detachViewDisposables();
            reset();
        }
    }

    public abstract void attachViewDisposables();

    public abstract void reset();

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        serviceDisposable.dispose();
        detachViewDisposables();
    }

    private void detachViewDisposables() {
        boxViewDisposables.clear();
    }

    protected void attachDisposable(Disposable d) {
        boxViewDisposables.add(d);
    }
}
