package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import wifeye.app.android.mahorad.com.wifeye.app.MainService;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

public class AndroidBootConsumer extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isBootAction(intent.getAction()))
            return;
        Intent service = new Intent(context, MainService.class);
        context.startService(service);
    }

    private boolean isBootAction(String action) {
        return action.equals(ACTION_BOOT_COMPLETED);
    }
}
