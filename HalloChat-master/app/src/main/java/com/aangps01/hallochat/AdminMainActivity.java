package com.aangps01.hallochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aangps01.hallochat.Fragments.FragAddPsikiater;
import com.aangps01.hallochat.Fragments.FragPsikiater;
import com.aangps01.hallochat.Fragments.FragUser;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HalloChat - Admin");
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminMainActivity.this,LoginActivity.class));
                finish();
                return true;
        }
        return false;
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(),1);
        adapter.addFragment(FragUser.newInstance(), "User");
        adapter.addFragment(FragPsikiater.newInstance(), "Psikolog");
        adapter.addFragment(FragAddPsikiater.newInstance(), "Tambah Psikolog");
        viewPager.setAdapter(adapter);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        SectionPagerAdapter(@NonNull FragmentManager manager, int behavior) {
            super(manager,behavior);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}