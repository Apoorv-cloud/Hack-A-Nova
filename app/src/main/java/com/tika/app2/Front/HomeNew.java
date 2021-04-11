package com.tika.app2.Front;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tika.app2.Adapter.ScreenSlidePagerAdapter;
import com.tika.app2.Login.Member;
import com.tika.app2.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class HomeNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String dial;
    ViewPager pager;
    BottomNavigationView navigation;
    Toolbar toolbar;

    String zap;


    DrawerLayout drawer;
    NavigationView navigationView;

    boolean firstOpen;

    FirebaseAuth firebaseAuth;
    FirebaseUser member;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private void makePhoneCall(){

        Log.i("TAG", "makephonecall     started: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        firebaseAuth= FirebaseAuth.getInstance();
        member=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member");
        String key=member.getEmail();
        databaseReference.addValueEventListener(new ValueEventListener() {
            String xyz;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member info = snapshot.getValue(Member.class);
                    if (key.equalsIgnoreCase(info.getName())) {
                        xyz = info.getPh().toString();
                        // a[i]=xyz;i=i+1;
                        String number = xyz;   //mEditTextNumber.getText().toString();
                        if (number.trim().length() > 0) {


                                dial = "tel:" + number;
                            Log.i("TAG", "makephonecall     datareceived: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                            Log.i("TAG", "makephonecall    "+dial+": $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

                            try {
                                generateNoteOnSD(getApplicationContext(),"numberdifesa",dial);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(HomeNew.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                        }


                    }
//                  makePhoneCall();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // makePhoneCall();
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) throws IOException {
        File root = new File(Environment.getExternalStorageDirectory(), "/Difesa");
        if (!root.exists()) {
            root.mkdirs();
        }

        Log.i("TAG", "saving: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");


        FileOutputStream fOut = null;


        String filepath =Environment.getExternalStorageDirectory().getAbsolutePath()+"/Difesa/number.txt";
        Log.i("TAG", "generateNoteOnSD: $$$$$$$$$$$$$$$$$$$"+ filepath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            byte[] buffer = dial.getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
            Log.i("TAG", "saved: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null)
                fos.close();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        makePhoneCall();

        navigation = findViewById(R.id.navigation);


        Intent intent = getIntent();
        firstOpen = intent.getBooleanExtra("firstOpen", false);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Home");

        Toolbar toolbar2 = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Home");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        pager = getViewPager();
        pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position == 0) {
                    navigation.setSelectedItemId(R.id.navigation_home);
                    toolbar.setTitle("Home");
                } else if (position == 1) {
                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                    toolbar.setTitle("Dashboard");
                } else if (position == 2) {
                    navigation.setSelectedItemId(R.id.navigation_Profile);
                    toolbar.setTitle("Profile");
                } else if (position == 3) {
                    navigation.setSelectedItemId(R.id.navigation_fourth);
                    toolbar.setTitle("Information Board");
                }
                else if (position == 4) {
                    toolbar.setTitle("Self Defense");
                }
                else if (position == 5) {
                    toolbar.setTitle("News");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (firstOpen == true) {
            Intent loginIntent = new Intent(this, loginpage.class);

            startActivity(loginIntent);
        } else {
            loadFragment(new HomeFragment());
        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        switch (id) {

            case R.id.register: {

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                pager.setCurrentItem(2);

                return true;
            }
            case R.id.maps: {

                Intent h = new Intent(HomeNew.this, PermissionsActivity.class);
                startActivity(h);

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.det: {

                pager.setCurrentItem(4);
                toolbar.setTitle("Self Defense");
                /*fragment = new InfantFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();*/
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }


            case R.id.sosmenu: {

                Intent h = new Intent(HomeNew.this, SosActivity.class);
                startActivity(h);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.shake: {

                Intent h = new Intent(HomeNew.this, MainXActivity.class);
                startActivity(h);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            case R.id.news: {

                pager.setCurrentItem(5);
                toolbar.setTitle("News");


                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;


            }


            case R.id.navigation_home: {
                pager.setCurrentItem(0);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;

            }

            case R.id.navigation_dashboard: {

                pager.setCurrentItem(1);

                return true;

            }

            case R.id.navigation_Profile: {

                pager.setCurrentItem(2);

                return true;

            }

            case R.id.navigation_fourth: {
                pager.setCurrentItem(3);

                return true;
            }
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {
            pager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            Intent intent = new Intent(HomeNew.this, loginpage.class);
            startActivity(intent);
            firebaseAuth.signOut();
            finish();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public ViewPager getViewPager() {
        if (null == pager) {
            pager = (ViewPager) findViewById(R.id.fragment_container);
        }
        return pager;
    }



}







