package Pro.Wash.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Pro.Wash.Client.Tab1;
import Pro.Wash.Client.Tab2;
import Pro.Wash.Client.Tab3;
import Pro.Wash.Client.Tab4;


public class PagerAdapter extends FragmentStatePagerAdapter {

    int NoOfTabs;

    public PagerAdapter(FragmentManager fm , int NoOfTabs) {
        super(fm);
        this.NoOfTabs = NoOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                //Order Accept Reject
                return new Tab1();

            case 1:
                //Out for Pickup
                return new Tab2();


            case 2:
                //Processesing your Order
                return new Tab3();

            case 3:
                //Out for Delivery
                return new Tab4();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
