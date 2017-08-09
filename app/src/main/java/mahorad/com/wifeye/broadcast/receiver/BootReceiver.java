package mahorad.com.wifeye.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import mahorad.com.wifeye.service.EngineService;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isBootAction(intent.getAction()))
            return;
        context.startService(new Intent(context, EngineService.class));
    }

    private boolean isBootAction(String action) {
        return action.equals(ACTION_BOOT_COMPLETED);
    }
}
