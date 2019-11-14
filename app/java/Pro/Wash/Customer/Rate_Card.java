package Pro.Wash.Customer;

import android.content.Intent;
import android.net.Uri;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import android.widget.TextView;

import Pro.Wash.Adapters.RateCardPagerAdapter;
import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;


public class Rate_Card extends AppCompatActivity implements
        RateCard1.OnFragmentInteractionListener
        ,RateCard2.OnFragmentInteractionListener
        ,RateCard3.OnFragmentInteractionListener {

    private ViewPager viewPager;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Rate_Card.this,HomeActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate__card);

        //Tab Layout
        TabLayout tabLayout = findViewById(R.id.TabLayoutRC);

        //View Pager
        viewPager = findViewById(R.id.pagerRC);

        tabLayout.addTab(tabLayout.newTab().setText("Wash And Fold"));
        tabLayout.addTab(tabLayout.newTab().setText("Wash And Iron"));
        tabLayout.addTab(tabLayout.newTab().setText("Dry Clean"));

        //Pager Adapter and Tab Settings
        RateCardPagerAdapter pagerAdapter = new RateCardPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                assert tab.getText() != null;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        //Toolbar
        Toolbar mToolbar = findViewById(R.id.RateCardToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rate_Card.this,HomeActivity.class));
                finish();
            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
