package mahorad.com.wifeye.di.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.scope.PerApplication;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@Module(includes = {  })
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

//    @Provides
//    @PerApplication
//    public Application application() {
//        return application;
//    }

    @Provides
    @PerApplication
    @ApplicationContext
    public Context context() {
        return application;
    }

    @Provides
    @PerApplication
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/helvetica_neue_light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    CompositeDisposable compositeDisposable() {
        return new CompositeDisposable();
    }
}
