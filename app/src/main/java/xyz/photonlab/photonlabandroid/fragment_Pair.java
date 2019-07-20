package xyz.photonlab.photonlabandroid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdBindable;
import com.github.druk.rx2dnssd.Rx2DnssdEmbedded;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import xyz.photonlab.photonlabandroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.List;


public class fragment_Pair extends Fragment implements wifiRvAdapter.OnNoteListener{


    static final String TAG = "fragment_Pair";

    pairing_Listener mlistener;

    List<ScanResult> results;
    wifiRvAdapter.OnNoteListener onNoteListener = this;
    String[] PERMS_INITIAL= {Manifest.permission.ACCESS_FINE_LOCATION};


    ScrollView step2_Layout;
    ConstraintLayout step1_layout;
    ConstraintLayout step3_Layout;
    ConstraintLayout step4_Layout;

    Button yes_Connected;
    Button back;
    Button connect;
    Button done;
    RecyclerView rv;
    EditText password_Input;
    ProgressBar progressBar;
    WebView webViewPair;
    EditText editText_Password;



    String apIpAddress;
    String TargetSSID;
    String TargetPassword;

    nsdFinder mnsdFinder;

    Rx2Dnssd rxDnssd;
    Disposable browseDisposable;




//    NsdManager nsdManager;
//    NsdManager.DiscoveryListener discoveryListener;

    public fragment_Pair(){

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxDnssd = new Rx2DnssdEmbedded(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_layout, container, false);

        final Context context = getActivity().getApplicationContext();
        //Yo, This is really shitty
        requestPermissions(PERMS_INITIAL,127);
       // setListener(mlistener);
       // mlistener.scan_Wifi();
        rv = (RecyclerView)view.findViewById(R.id.wifiRV);
        progressBar = view.findViewById(R.id.progressBar);
        yes_Connected = view.findViewById(R.id.YesConnected);
        back = view.findViewById(R.id.backButton_Pairing);
        done = view.findViewById(R.id.done);
        connect = view.findViewById(R.id.connect_Button);

        step1_layout = view.findViewById(R.id.step1);
        step2_Layout = view.findViewById(R.id.step2);
        step3_Layout = view.findViewById(R.id.step3);
        step4_Layout = view.findViewById(R.id.step4);

        password_Input = view.findViewById(R.id.edit_Password);
        webViewPair = view.findViewById(R.id.webViewPair);


        final WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);



        step2_Layout.setVisibility(View.INVISIBLE);
        step3_Layout.setVisibility(View.INVISIBLE);
        step4_Layout.setVisibility(View.INVISIBLE);
        progressBar.setProgress(25);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = progressBar.getProgress();
                switch (progress){
                    case 25:
                        getActivity().getSupportFragmentManager().popBackStack();
                        return;
                    case 50:
                        progressBar.setProgress(25);
                        rv.setVisibility(View.GONE);
                        rv.setFocusable(false);
                        rv.setClickable(false);
                        yes_Connected.setClickable(true);
                        Blayout_Gone(step2_Layout, View.INVISIBLE);
                        Blayout_Show(step1_layout);
                        return;
                    case 75:
                        progressBar.setProgress(50);
                        rv.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.VISIBLE);
                        password_Input.setVisibility(View.GONE);
                        password_Input.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        connect.setClickable(false);
                        connect.setVisibility(View.GONE);
                        Blayout_Gone(step3_Layout, View.INVISIBLE);
                        Blayout_Show(step2_Layout);
                        return;
                    case 100:
                        progressBar.setProgress(75);
                        password_Input.setVisibility(View.VISIBLE);
                        connect.setVisibility(View.VISIBLE);
                        connect.setClickable(true);
                        done.setVisibility(View.INVISIBLE);
                        Blayout_Gone(step4_Layout, View.INVISIBLE);
                        Blayout_Show(step3_Layout);
                        return;


                }
            }
        });

        yes_Connected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_Gone(step1_layout, View.INVISIBLE);
                layout_Show(step2_Layout);
//                step2_Layout.setVisibility(View.VISIBLE);
//                step1_layout.setVisibility(View.GONE);
                progressBar.setProgress(50);
                yes_Connected.setClickable(false);
                int tempIP = wifiManager.getDhcpInfo().gateway;
                apIpAddress = ipToString(tempIP);
                rv.setVisibility(View.VISIBLE);
                rv.setClickable(true);
                rv.setFocusable(true);

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_Show(step4_Layout);
                layout_Gone(step3_Layout,View.INVISIBLE);
                progressBar.setProgress(100);
                connect.setVisibility(View.GONE);
                connect.setClickable(false);
                password_Input.setVisibility(View.GONE);
                password_Input.onEditorAction(EditorInfo.IME_ACTION_DONE);
                done.setVisibility(View.VISIBLE);
                webViewPair.loadUrl("http://"+ apIpAddress+"/join?ssid="+ TargetSSID
                                    +"&password="+password_Input.getText().toString());
                Log.d(TAG, "onClickConnect: "+"http://"+ apIpAddress+"/"+"wifiPassword"+"/"+password_Input.getText().toString());

//                new JsonTask().execute("http://elementlight.local/ip");
                Log.d(TAG, "onClick: start discovery");


                mnsdFinder = new nsdFinder(getContext());
                mnsdFinder.start();

                browseDisposable = rxDnssd.browse("_elementlight._udp", "local.")
                        .compose(rxDnssd.resolve())
                        .compose(rxDnssd.queryRecords())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bonjourService -> {
                            Log.d("TAG",  bonjourService.getInet4Address().toString());
//            if (bonjourService.isLost()) {
//                mServiceAdapter.remove(bonjourService);
//            } else {
//                mServiceAdapter.add(bonjourService);
//            }
                        }, throwable -> Log.e("TAG", "error", throwable));




            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    results = wifiManager.getScanResults();
                    Log.d("SCAN", "onReceive:scan success ");
                    LinearLayoutManager llm = new LinearLayoutManager(context);
                    rv.setLayoutManager(llm);
                    wifiRvAdapter adapter = new wifiRvAdapter(results, onNoteListener);


                    rv.setAdapter(adapter);
                } else {
                    // scan failure handling
                    Log.d("SCAN", "onReceive:scan fail ");
                    scanFailure(wifiManager);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);

        Log.d(TAG, "onCreateView: Does it start scan?");
        wifiManager.setWifiEnabled(true);

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            Log.d(TAG, "onCreateView: scan not success");
            scanFailure(wifiManager);
        }


        return view ;
    }

    private void scanSuccess(WifiManager wifiManager) {
        results = wifiManager.getScanResults();
    }

    private void scanFailure(WifiManager wifiManager) {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        results = wifiManager.getScanResults();
    }

    @Override
    public void onNoteClick(int position) {


        layout_Show(step3_Layout);
        layout_Gone(step2_Layout, View.INVISIBLE);
        progressBar.setProgress(75);
        TargetSSID = results.get(position).SSID;
        rv.setVisibility(View.GONE);
        rv.setClickable(false);
        password_Input.setVisibility(View.VISIBLE);
        connect.setVisibility(View.VISIBLE);

        //webViewPair.loadUrl("http://"+ apIpAddress+"/"+"wifiName"+"/"+TargetSSID);

        Log.d(TAG, "onNoteClick: "+apIpAddress+"/"+TargetSSID);


    }


    public void layout_Show(View view){
        TranslateAnimation animate = new TranslateAnimation(view.getWidth(),0,0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);

    }

    // To animate view slide out from right to left
    public void layout_Gone(View view, int visibility){
        TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(visibility);
    }

    public void Blayout_Gone(View view, int visibility){
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(visibility);
    }

    public void Blayout_Show(View view){
        TranslateAnimation animate = new TranslateAnimation(-view.getWidth(),0,0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public String ipToString(int tempIP){
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            tempIP = Integer.reverseBytes(tempIP);
        }

        byte[] ipByteArray = BigInteger.valueOf(tempIP).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }



    public interface pairing_Listener {
        //TODO: return back the ip address of the device -Bonny
        public void mainControl(String Tag, int value);
        public int rounterIpAddress(int address);
    }

    public void setListener(pairing_Listener mlistener){
        this.mlistener = mlistener;

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute: ");

//            pd = new ProgressDialog(MainActivity.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "onPostExecute: "+result);
            if(result!=null) {
                String temp = result.replaceAll("\\{", " ");
                temp = temp.replaceAll("\"", " ");
                Log.d(TAG, "onPostExecute:place " + temp);
                String[] a = result.split(",");
                for(int i = 0; i< a.length;i++){
                    Log.d(TAG, "onPostExecute: "+a[i]);
                }
            }

        }
    }



//    public void initializeDiscoveryListener() {
//
//        // Instantiate a new DiscoveryListener
//        discoveryListener = new NsdManager.DiscoveryListener() {
//
//
//            // Called as soon as service discovery begins.
//            @Override
//            public void onDiscoveryStarted(String regType) {
//                Log.d(TAG, "Service discovery started");
//            }
//
//
//            @Override
//            public void onServiceFound(NsdServiceInfo service) {
//                // A service was found! Do something with it.
//
//                Log.d(TAG, "Service discovery success" + service);
////                if (!service.getServiceType().equals(SERVICE_TYPE)) {
////                    // Service type is the string containing the protocol and
////                    // transport layer for this service.
////                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
////                } else if (service.getServiceName().equals(serviceName)) {
////                    // The name of the service tells the user what they'd be
////                    // connecting to. It could be "Bob's Chat App".
////                    Log.d(TAG, "Same machine: " + serviceName);
////                } else if (service.getServiceName().contains("NsdChat")){
////                    nsdManager.resolveService(service, resolveListener);
////                }
//            }
//
//            @Override
//            public void onServiceLost(NsdServiceInfo service) {
//                // When the network service is no longer available.
//                // Internal bookkeeping code goes here.
//                Log.e(TAG, "service lost: " + service);
//            }
//
//            @Override
//            public void onDiscoveryStopped(String serviceType) {
//                Log.i(TAG, "Discovery stopped: " + serviceType);
//            }
//
//            @Override
//            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
//                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
//                nsdManager.stopServiceDiscovery(this);
//            }
//
//            @Override
//            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
//                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
//                nsdManager.stopServiceDiscovery(this);
//            }
//        };
//    }


}
