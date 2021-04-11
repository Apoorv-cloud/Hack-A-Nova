package com.tika.app2.Front;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.tika.app2.Home.VoiceRecorder;
import com.tika.app2.R;


public class HomeFragment extends Fragment implements View.OnClickListener {

    View view;


    ImageView mic,shake,siren,getLocBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        shake = view.findViewById(R.id.shake);
        mic =  view.findViewById(R.id.mic_home);
        getLocBtn =  view.findViewById(R.id.police_stn_home);
        siren = view.findViewById(R.id.siren);
        final MediaPlayer sirenMP = MediaPlayer.create(getActivity(), R.raw.siren);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voiceRecordIntent= new Intent(getActivity() ,VoiceRecorder.class);
                startActivity(voiceRecordIntent);

            }
        });
        getLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent locIntent= new Intent(getActivity(), MapActivity.class);
                startActivity(locIntent);

            }
        });

        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shakeIntent= new Intent(getActivity() ,MainXActivity.class);
                startActivity(shakeIntent);

            }
        });

        siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                sirenMP.start();

            }});


        return view;
    }


    @Override
    public void onClick(View v) {

    }
}