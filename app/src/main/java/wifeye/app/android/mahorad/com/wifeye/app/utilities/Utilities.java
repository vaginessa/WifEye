package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;

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

    public String toAgo(final Date date, Context context){
        return new PrettyTime(getLocale(context)).format(date);
    }

    public Locale getLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return context
                    .getResources()
                    .getConfiguration()
                    .getLocales()
                    .get(0);
        return context
                .getResources()
                .getConfiguration()
                .locale;
    }

    public Uri getResourceUri(int id, Context context) {
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + res.getResourcePackageName(id)
                + "/" + res.getResourceTypeName(id)
                + "/" + res.getResourceEntryName(id));
    }

    public int dip2px(float dpValue, Context context) {
        float scale = context
                .getResources()
                .getDisplayMetrics()
                .density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void openPermissions(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().equals(""));
    }

    public static boolean areEqual(String s1, String s2) {
        if (s1 == null && s2 == null) return true;
        boolean anyNull = (s1 == null || s2 == null);
        return !anyNull && s1.equals(s2);
    }
}
