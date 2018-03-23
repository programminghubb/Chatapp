package com.programminghub.simplechat;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

   TabLayout tabLayout;
   ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        defineView();
        bindView();


    }
    private void defineView(){
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.main_viewpager);
    }

    private void bindView(){
        setupViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //  adapter.addFragment(new CuriosityModeFeatured(),"Featured");
        adapter.addFragment(new ShowUserListFragment().newInstance(setUpUserList()), "Users");
        adapter.addFragment(new ShowUserListFragment().newInstance(setUpChatListUsers()),"Chats");
        adapter.addFragment(new ShowUserListFragment().newInstance(setUpOnlineUsers()),"Online");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    private List<User> setUpUserList(){
        final List<User> userList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("simpleChat").child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Iterator<DataSnapshot> iterator=  dataSnapshot.getChildren().iterator();
               while (iterator.hasNext()){
                   DataSnapshot snapshot=iterator.next();
                   userList.add(snapshot.getValue(User.class));
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userList;
    }
    private List<User> setUpChatListUsers(){
        List<User> userList=new ArrayList<>();
        userList.add(new User("ram@gmail.com","1",""));
        return userList;
    }
    private List<User> setUpOnlineUsers(){
        List<User> userList=new ArrayList<>();
        userList.add(new User("ram@gmail.com","1",""));
        userList.add(new User("hari@gmail.com","3",""));
        return userList;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
