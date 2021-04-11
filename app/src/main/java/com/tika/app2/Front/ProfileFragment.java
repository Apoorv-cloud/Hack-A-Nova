package com.tika.app2.Front;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tika.app2.Home.aadharupload;
import com.tika.app2.Login.Member;
import com.tika.app2.Medical.Pedometer;
import com.tika.app2.R;

public class ProfileFragment extends Fragment {
    //    PieChart mPieChart;
    Button pdfBtn,steps;
    private TextView we,he,b,cpn,n;
    FirebaseAuth firebaseAuth;
    FirebaseUser member;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView call;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.profile,container,false);
        we=view.findViewById(R.id.weight_no);
        he=view.findViewById(R.id.lam);
        b=view.findViewById(R.id.blood);
        cpn=view.findViewById(R.id.call_profile_number);
        setRetainInstance(true);
        pdfBtn = (Button) view.findViewById(R.id.pdfupload_btn);
        steps = (Button) view.findViewById(R.id.pedo_button);
        call = (ImageView) view.findViewById(R.id.call_button_profile);
        n=view.findViewById(R.id.prof_name);

        /*mPieChart = (PieChart) getActivity().findViewById(R.id.piechart);
        mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
*/
        fetch();
//        ImageView callButton =(ImageView) view.findViewById(R.id.call_button_profile);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");


        pdfBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                Intent intent = new Intent(getActivity(),aadharupload.class);
                startActivity(intent);

            }

        });
        steps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(getActivity(), Pedometer.class);
                startActivity(intent);

            }

        });

        call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PackageManager packageManager=getActivity().getPackageManager();
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
                    TextView e = getActivity().findViewById(R.id.call_profile_number);

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+e.getText().toString()));
                    startActivity(intent);

                }
            }
        });


        return view;
    }
    public void fetch()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        member=firebaseAuth.getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Member");
        String key=member.getEmail();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member info = snapshot.getValue(Member.class);
                    if (key.equalsIgnoreCase(info.getName())) {
                        int x = info.getW();
                        String y = Integer.toString(x);
                        we.setText(y);
                    }
                    if (key.equalsIgnoreCase(info.getName())) {
                        int x = info.getH();
                        String y = Integer.toString(x);
                        he.setText(y);
                    }
                    if (key.equalsIgnoreCase(info.getName())) {
                        String y= info.getBg();

                        b.setText(y);
                    }
                    if (key.equalsIgnoreCase(info.getName())) {
                        Long x = info.getPh();
                        String y = Long.toString(x);
                        cpn.setText(y);
                    }
                    if (key.equalsIgnoreCase(info.getName())) {
                        String y= info.getN();

                        n.setText(y);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}