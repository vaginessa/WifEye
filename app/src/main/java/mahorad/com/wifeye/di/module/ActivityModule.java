package mahorad.com.wifeye.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import mahorad.com.wifeye.di.qualifier.ActivityContext;

@Module(includes = {/* include modules to fulfill its required dependencies */})
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    public Context context() {
        return activity;
    }

//    @Provides
//    public AppCompatActivity activity() {
//        return activity;
//    }

    @Provides
    CompositeDisposable compositeDisposable() {
        return new CompositeDisposable();
    }


}
