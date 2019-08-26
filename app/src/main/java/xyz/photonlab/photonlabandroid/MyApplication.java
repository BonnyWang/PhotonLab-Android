package xyz.photonlab.photonlabandroid;

import android.app.Application;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import xyz.photonlab.photonlabandroid.utils.NetworkNodeScanner;

/**
 * created by KIO on 2019/8/20
 */
public class MyApplication extends Application {

    private final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            new NetworkNodeScanner(InetAddress.getByName("192.168.1.1"), InetAddress.getByName("255.255.255.0")).scan();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        String[] ips = new String[]{
//                "192.168.1.1",
//                "192.168.1.101",
//                "192.168.1.104",
//                "192.168.1.105"
//        };
//        for (int i = 0; i < 10; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    Process p = Runtime.getRuntime().exec("ping -w 1 " + ips[finalI % ips.length]);
//                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        if (line.contains("ttl")) {
//                            Log.e(TAG, line + ips[finalI % ips.length]);
//                        } else {
//                            Log.i(TAG, line);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }

//        try {
//            Process p = Runtime.getRuntime().exec("ping -w 1 192.168.1.106");
//            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (line.contains("ttl")) {
//                    Log.e(TAG, line);
//                } else {
//                    Log.i(TAG, line);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onLowMemory() {
        Log.w(TAG, "Low Memory");
        super.onLowMemory();
    }
}
