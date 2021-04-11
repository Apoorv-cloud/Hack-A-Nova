package com.tika.app2.Bluetooth;



import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tika.app2.R;

import java.io.IOException;
import java.util.UUID;

public class Receiver extends AppCompatActivity {

    // Widgets
    public Button btnDis;
    public TextView dataTW;
    public String address = null;

    // Bluetooth
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBTConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Thread workerThread;
    byte[] generalBuffer;
    int generalBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        address="00:21:13:01:14:D1";
        setContentView(R.layout.activity_receiver);

        // Initialize widgets
        btnDis = (Button)findViewById(R.id.BTN_disc);
        dataTW = (TextView)findViewById(R.id.TW_data);

        new ConnectBT().execute();

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
            }
        });
    }


    @Override
    public void onBackPressed() {
        disconnect();
        finish();
        return;
    }

    // Disconnection
    private void disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            }
            catch (IOException e) {
                toast("Error Closing Socket");
            }
        }
        finish();
    }

    private void toast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 35;

        stopWorker = false;
        generalBufferPosition = 0;
        generalBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {

                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = btSocket.getInputStream().available();

                        if (bytesAvailable > 0) {
                            byte[] packet = new byte[bytesAvailable];
                            btSocket.getInputStream().read(packet);

                            for (int i=0; i<bytesAvailable; i++) {
                                byte b = packet[i];
                                if (b == delimiter) {
                                    byte[] arrivedBytes = new byte[generalBufferPosition];
                                    System.arraycopy(generalBuffer, 0, arrivedBytes, 0, arrivedBytes.length);
                                    final String data = new String(arrivedBytes, "US-ASCII");
                                    generalBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            dataTW.setText(data+" °C");
                                        }
                                    });
                                }
                                else {
                                    generalBuffer[generalBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean connectionSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Receiver.this, "Connecting...", "Please wait!");
        }

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (btSocket == null || !isBTConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = myBluetooth.getRemoteDevice(address);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e) {
                connectionSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!connectionSuccess) {
                toast("Connection Failed. Try again.");
                finish();
            }
            else {
                toast("Connected.");
                beginListenForData();
                isBTConnected = true;
            }
            progress.dismiss();
        }
    }
}