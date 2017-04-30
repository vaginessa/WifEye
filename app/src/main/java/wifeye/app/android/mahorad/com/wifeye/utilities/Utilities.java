package wifeye.app.android.mahorad.com.wifeye.utilities;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;

public class Utilities {

    public boolean isServiceRunning(Class<?> serviceClass) {
        if (serviceClass == null) return false;
        String targetServiceName = serviceClass.getName();
        for (RunningServiceInfo service : getRunningServices()) {
            String serviceName = service.service.getClassName();
            if (targetServiceName.equals(serviceName))
                return true;
        }
        return false;
    }

    public List<RunningServiceInfo> getRunningServices() {
        ActivityManager manager = getActivityManager();
        return manager.getRunningServices(Integer.MAX_VALUE);
    }

    private ActivityManager getActivityManager() {
        return (ActivityManager) MainApplication
                    .mainComponent()
                    .context()
                    .getSystemService(Context.ACTIVITY_SERVICE);
    }
}
