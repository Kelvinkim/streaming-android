package red5pro.org.testandroidproject.tests.RecordCuePointsTest;

import android.util.Log;

import com.red5pro.streaming.event.R5ConnectionEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import red5pro.org.testandroidproject.tests.RecordedTest.RecordedTest;


public class RecordCuePointsTest extends RecordedTest {


    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> future;
    double time = 0.0;
    long starttime = 0;

    @Override
    public void onConnectionEvent(R5ConnectionEvent r5ConnectionEvent) {


        super.onConnectionEvent(r5ConnectionEvent);
        if(r5ConnectionEvent == R5ConnectionEvent.START_STREAMING )
        {
            starttime = System.currentTimeMillis();

            // streaming started
            future = executor.scheduleAtFixedRate(new CuePointPublisher(), 0, 1, TimeUnit.SECONDS);

        }else if(r5ConnectionEvent == R5ConnectionEvent.DISCONNECTED ) {

            // streaming stopped
            future.cancel(true);
            executor.shutdownNow();
        }
    }



    class CuePointPublisher implements Runnable {

        @Override
        public void run() {

            try
            {
                Log.d("CuePointPublisher", "Sending onCuePoint");

                time = (System.currentTimeMillis()-starttime) / 1000;

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("time", time);
                map.put("name", "timetrack");
                map.put("type", "android");
                publish.send("onCuePoint", map);
            }
            catch(Exception e)
            {

            }
        }
    }
}
