package xyz.photonlab.photonlabandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnection {

    private static final String TAG = "BluetoothClass";

    private static final String app_Name = "PHLProto";
    private static final UUID my_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private final BluetoothAdapter mbluetoothAdapter;
    Context mcontext;

    private AcceptThread macceptThread;
    private ConnectThread mconnectThread;
    private BluetoothDevice mmdevice;
    private UUID deviceUUID;
    private ConnectedThread mconnectedThread;

    public BluetoothConnection(Context context) {
        mcontext = context;
        mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
        Log.d("!!!!!!!!!!!!", "BluetoothConnection: Constructor ");
    }

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mserverSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try {

                tmp = mbluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(app_Name, my_UUID);
            }catch (IOException e){

            }

            mserverSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread ");

            BluetoothSocket mSocket = null;

            try{
                mSocket = mserverSocket.accept();
            }catch (IOException e){

            }

            if(mSocket!=null){
                //TODO:Add see the third video
                connected(mSocket,mmdevice);
            }
        }

        public void cancel(){
            try {
                mserverSocket.close();
            }catch (IOException e){

            }
        }
    }

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            mmdevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;

            try {
                tmp = mmdevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            }catch (IOException e){

            }
            mmSocket = tmp;

            mbluetoothAdapter.cancelDiscovery();

            try{
                mmSocket.connect();
            }catch (IOException e){
                try{
                    mmSocket.close();
                }catch (IOException ee){

                }
            }

            connected(mmSocket,mmdevice);


        }

        public void cancel(){
            try {
                mmSocket.close();
            }catch (IOException e){

            }
        }
    }


    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmmSocket;
        private final InputStream mmInputstream;
        private final OutputStream mmOutputstream;

        public ConnectedThread(BluetoothSocket socket){
            mmmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = mmmSocket.getInputStream();
                tmpOut = mmmSocket.getOutputStream();
            }catch (IOException e){

            }

            mmInputstream = tmpIn;
            mmOutputstream = tmpOut;

        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true){
                try {
                    bytes = mmInputstream.read(buffer);
                    String inMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "Incomming Message is: " + inMessage);
                }catch (IOException e){
                    break;
                }
            }
        }

        public void write(byte[] bytes){
            //only for display the text, no actual use -Bonny
            String text = new String(bytes, Charset.defaultCharset());
            try {
                //Can also be integer -Bonny
                mmOutputstream.write(bytes);
            }catch (IOException e){

            }
        }

        public void cancel(){
            try {
                mmmSocket.close();
            }catch (IOException e){

            }
        }
    }

    //Synchronized method is a method which can be used by only one thread at a time. -Bonny
    public synchronized void start(){
        if(mconnectThread!= null){
            mconnectThread.cancel();
            mconnectThread = null;
        }

        if(macceptThread == null){
            macceptThread = new AcceptThread();
            macceptThread.start();
        }

    }

    public void startClient(BluetoothDevice device, UUID uuid){
        //mconnectThread = new ConnectThread(device,uuid);
        //mconnectThread.start();
    }

    private void connected(BluetoothSocket mmsocket, BluetoothDevice mmdevice){
        mconnectedThread = new ConnectedThread(mmsocket);
        mconnectedThread.start();
    }

    public void write(byte[] out){
        mconnectedThread.write(out);
    }

    public void test(){
        Log.d(TAG, "test: !!!!!");
    }

}
