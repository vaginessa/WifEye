package mahorad.com.wifeye.base;

import android.app.Service;

import dagger.android.AndroidInjection;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

}
