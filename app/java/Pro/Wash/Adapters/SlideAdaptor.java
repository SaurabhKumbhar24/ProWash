package Pro.Wash.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Pro.Wash.R;

public class SlideAdaptor extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    //List of Images
    public int[] ImageList = {
            R.drawable.slider2,
            R.drawable.slider7,
            R.drawable.slider4,
            R.drawable.slider5,
            R.drawable.slider6
    };

    //List of TextView Title
    public String[] TitleList = {
            "Ease Of Placing Order",
            "Your Time Is Precious",
            "Meet Our Delivery Boy",
            "Hygiene Is Our Priority",
            "Dress Up In Style"
    };

    //List of Descriptions
    public String[] DescriptionList = {
            "With few easy steps you can place order.",
            "You can choose convenient pickup date and time slot.",
            "We collect your clothes from doorstep",
            "We assured germs and dirt free clothes",
            "We bring your clothes back fresh within specified time"
    };

    //List of Background
    public int[] BackgroundList[] = {

    };

    public SlideAdaptor(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return TitleList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.slider,container,false);

        //Image View
        ImageView sliderImages = view.findViewById(R.id.slideimg);

        //Text View
        TextView sliderTitle = view.findViewById(R.id.sliderTitle); // Title
        TextView sliderDescription = view.findViewById(R.id.sliderdescription); // Description

        //sliderLayout.setBackgroundResource(BackgroundList[position]);
        sliderImages.setImageResource(ImageList[position]);
        sliderTitle.setText(TitleList[position]);
        sliderDescription.setText(DescriptionList[position]);


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
