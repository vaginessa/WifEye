package mahorad.com.wifeye.util;

import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class Constants {

    public static final int OBSERVE_REPEAT_COUNT = 10;
    public static final int WIFI_ENABLE_TIMEOUT = 90;
    public static final int WIFI_DISABLE_TIMEOUT = 45;

    public static String[] PERMISSIONS = {
            PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
            PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
            PermissionUtils.Manifest_READ_EXTERNAL_STORAGE
    };

    public static final String MESSAGE_FAILURE_COARSE_LOCATION
            = "No permitted to receive cell tower information from phone";

    public static final String MESSAGE_FAILURE_PERSIST_CTID
            = "No permitted to save cell tower information on phone";

    public static final String PARCELABLE_RESULT_RECEIVER = "ResultReceiver";


}