package mahorad.com.wifeye.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import mahorad.com.wifeye.di.qualifier.ActivityContext;
import mahorad.com.wifeye.di.scope.PerActivity;
import mahorad.com.wifeye.di.scope.PerApplication;
import mahorad.com.wifeye.ui.overview.OverviewFragment;
import mahorad.com.wifeye.ui.overview.OverviewViewModel;

@Module(includes = {  })
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

//    @Provides
//    @PerActivity
//    public AppCompatActivity activity() {
//        return activity;
//    }

    @Provides
    @PerActivity
    @ActivityContext
    public Context context() {
        return activity;
    }

    @Provides
    @PerActivity
    CompositeDisposable compositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @PerActivity
    OverviewFragment overviewFragment() {
        return new OverviewFragment();
    }

    @Provides
    @PerActivity
    OverviewViewModel overviewViewModel() {
        return new OverviewViewModel();
    }
}
