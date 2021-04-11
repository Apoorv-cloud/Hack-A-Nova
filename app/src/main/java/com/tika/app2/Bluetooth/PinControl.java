package com.tika.app2.Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tika.app2.R;

import java.io.IOException;
import java.util.UUID;

public class PinControl extends AppCompatActivity {

    Button btnOn, btnOff, btnDis;
    SeekBar powerBar;
    TextView pwrTW;


    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBTConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newInt = getIntent();
        address = newInt.getStringExtra(DeviceList.EXTRA_ADDRESS);

        setContentView(R.layout.activity_pin_control);

        btnOn = (Button)findViewById(R.id.on_btn);
        btnOff = (Button)findViewById(R.id.off_btn);
        btnDis = (Button)findViewById(R.id.disc_btn);
        powerBar = (SeekBar)findViewById(R.id.pwr_seekbar);
        pwrTW = (TextView)findViewById(R.id.pwr);

        new ConnectBT().execute();

        btnOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOffLed();
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
            }
        });

        powerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    int percent = (int)((progress/255.0)*100);
                    pwrTW.setText(percent + "%");
                    try {
                        btSocket.getOutputStream().write(progress);
                    }
                    catch (IOException e) {
                        toast("Error Sending Data");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        disconnect();
        finish();
        return;
    }


    private void disconnect() {
        turnOffLed();
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

    private void turnOffLed() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(0);
                powerBar.setProgress(0);
                pwrTW.setText("0%");
            }
            catch (IOException e) {
                toast("Error Sending Data");
            }
        }
    }

    private void turnOnLed() {
        if (btSocket!=null) {
            try {
                btSocket.getOutputStream().write(255);
                powerBar.setProgress(255);
                pwrTW.setText("100%");
            }
            catch (IOException e) {
                toast("Error Sending Data");
            }
        }
    }

    private void toast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean connectionSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(PinControl.this, "Connecting...", "Please wait!");
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
                isBTConnected = true;
            }
            progress.dismiss();
        }
    }
}