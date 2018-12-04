package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.adapters.MudarOnboarding;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;



public class Onboarding extends AppCompatActivity {


        private ViewPager viewPager;
        private TabLayout tabLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_onboarding);

            viewPager = (ViewPager) findViewById(R.id.pager);
            tabLayout = (TabLayout) findViewById(R.id.tabslayout);

            final MudarOnboarding adapter = new MudarOnboarding(this.getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setSelectedTabIndicatorHeight(16);
        }


}
