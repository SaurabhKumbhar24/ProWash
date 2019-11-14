package Pro.Wash.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Pro.Wash.Customer.RateCard1;
import Pro.Wash.Customer.RateCard2;
import Pro.Wash.Customer.RateCard3;

public class RateCardPagerAdapter extends FragmentStatePagerAdapter {

    int NoOfTabs;

    public RateCardPagerAdapter(FragmentManager fm , int NoOfTabs) {
        super(fm);
        this.NoOfTabs = NoOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:

                return new RateCard1();

            case 1:

                return new RateCard2();


            case 2:
                return new RateCard3();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
