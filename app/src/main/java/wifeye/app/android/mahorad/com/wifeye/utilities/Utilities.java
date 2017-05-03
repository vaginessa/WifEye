package wifeye.app.android.mahorad.com.wifeye.utilities;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;

public class Utilities {

    public boolean isRunning(Class<?> serviceClass) {
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
                    .appComponent()
                    .context()
                    .getSystemService(Context.ACTIVITY_SERVICE);
    }

    public String simpleDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }
}
