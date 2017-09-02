package mahorad.com.wifeye.ui.custom.box;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.base.BaseView;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Created by mahan on 2017-08-18.
 */

public abstract class AbstractBoxView extends BoxView implements BaseView {

    private static final String TAG = AbstractBoxView.class.getSimpleName();

    private CompositeDisposable disposables;

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
        onSetup();
        if (disposables != null) return;
        Timber.tag(TAG).i("layout inflated");
        disposables = new CompositeDisposable();
        disposables.add(refreshDisposable());
        attachViewDisposables();
        RxView.clicks(this).subscribe(this::onClick);
    }

    private Disposable refreshDisposable() {
        return Observable
                .interval(1, MINUTES)
                .doOnError(Timber::e)
                .observeOn(mainThread())
                .doOnNext(l -> refresh())
                .subscribe();
    }

    protected abstract void onClick(Object o);

    public abstract void attachViewDisposables();

    protected void attachDisposable(Disposable d) {
        disposables.add(d);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Timber.tag(TAG).i("detached from window");
        disposables.clear();
        disposables.dispose();
        disposables = null;
    }

    @Override
    public void onSetup() {
        setupHeader();
        setupContent();
        setupFact();
        setupCaption();
    }

    protected abstract void setupHeader();

    protected abstract void setupContent();

    protected abstract void setupFact();

    protected abstract void setupCaption();

    @Override
    @CallSuper
    public void refresh() {
        refreshHeader();
        refreshContent();
        refreshFact();
        refreshCaption();
    }

    protected abstract void refreshHeader();

    protected abstract void refreshFact();

    protected abstract void refreshContent();

    protected abstract void refreshCaption();

}
