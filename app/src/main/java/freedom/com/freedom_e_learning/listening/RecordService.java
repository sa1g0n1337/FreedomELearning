package freedom.com.freedom_e_learning.listening;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RecordService extends Service {
    public RecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        // TODO: Make Record Audio
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // TODO: Audio Record
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        // TODO: Stop Record
        super.onDestroy();
    }

}
