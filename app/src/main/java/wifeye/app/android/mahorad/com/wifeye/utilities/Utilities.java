package wifeye.app.android.mahorad.com.wifeye.utilities;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;

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

    public Uri getResourceUri(int id, Context context) {
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + res.getResourcePackageName(id)
                + "/" + res.getResourceTypeName(id)
                + "/" + res.getResourceEntryName(id));
    }

    public void openPermissions(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
