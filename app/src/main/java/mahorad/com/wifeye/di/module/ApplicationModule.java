package mahorad.com.wifeye.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.engine.Engine;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

@Module
public abstract class ApplicationModule {

    @Binds
    @ApplicationContext
    abstract Context context(Application application);

    @Singleton
    @Provides
    static CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig
                .Builder()
                .setDefaultFontPath("font/helvetica_neue_light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    static CompositeDisposable compositeDisposable() {
        return new CompositeDisposable();
    }

    @Singleton
    @Provides
    static Engine engine() {
        return new Engine();
    }

}
