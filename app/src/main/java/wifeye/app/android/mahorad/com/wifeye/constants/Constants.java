package wifeye.app.android.mahorad.com.wifeye.constants;

import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class Constants {

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