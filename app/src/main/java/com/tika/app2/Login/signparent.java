package com.tika.app2.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tika.app2.Front.loginpage;
import com.tika.app2.R;

public class signparent extends AppCompatActivity {

    EditText txtEmail,txtPassword,txtDob,phone,phone2,ht,wt,bgroup,name,nem,phone3;
    Button btn_register;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signparent);


        txtEmail = (EditText)findViewById(R.id.email);
        txtPassword = (EditText)findViewById(R.id.pass);
        txtDob = (EditText)findViewById(R.id.dob);
        btn_register = (Button)findViewById(R.id.regt);
        ht=(EditText)findViewById(R.id.height);
        wt=(EditText)findViewById(R.id.weight);
        bgroup=(EditText)findViewById(R.id.b_group);
        phone=(EditText)findViewById(R.id.phone_num);
        phone3=(EditText)findViewById(R.id.phone_num22);
        phone2=(EditText)findViewById(R.id.phone_num2);
        name= (EditText)findViewById(R.id.name);
        nem=(EditText)findViewById(R.id.name);
        firebaseAuth = FirebaseAuth.getInstance();

        reff= FirebaseDatabase.getInstance().getReference().child("Member");
        member=new Member();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                Long phn=Long.parseLong(phone.getText().toString().trim());
                Long phn3=Long.parseLong(phone3.getText().toString().trim());
                int weight=Integer.parseInt((wt.getText().toString().trim()));
                int height=Integer.parseInt((ht.getText().toString().trim()));
                String bg=bgroup.getText().toString().trim();
                String na=name.getText().toString().trim();

                member.setPh(phn);
                member.setH(height);
                member.setW(weight);
                member.setBg(bg);
                member.setName(email);
                member.setN(na);
                member.setPh2(phn3);
                member.setLa("28.7226794");
                member.setLon("77.1415056");

                reff.push().setValue(member);

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(signparent.this,"Please Enter Email",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(signparent.this,"Please Enter Password",Toast.LENGTH_SHORT);
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signparent.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    startActivity(new Intent(getApplicationContext(), loginpage.class));

                                } else {
                                    Toast.makeText(signparent.this,"Authentication Failed",Toast.LENGTH_SHORT);
                                }

                                // ...
                            }
                        });


            }
        });





    }
}
