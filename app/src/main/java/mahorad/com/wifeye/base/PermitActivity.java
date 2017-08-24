package mahorad.com.wifeye.base;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

/**
 * request all permissions which activity needs in order to work
 */
public class PermitActivity extends ActivityManagePermission {

    protected void request(String... permissions) {
        askCompactPermissions(permissions, new PermissionResult() {
            @Override
            public void permissionGranted() { onPermissionsGranted(); }
            @Override
            public void permissionDenied() { onPermissionsDenied(); }
            @Override
            public void permissionForeverDenied() {
                onPermissionsBanned();
            }
        });
    }

    protected void onPermissionsGranted() {
    }

    protected void onPermissionsDenied() {
    }

    protected void onPermissionsBanned() {
    }


}
