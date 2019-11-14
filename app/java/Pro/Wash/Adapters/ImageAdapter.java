package Pro.Wash.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import Pro.Wash.R;

public class ImageAdapter extends BaseAdapter {

    public Integer[] mThumbIds = {
            R.drawable.male1,
            R.drawable.male2,
            R.drawable.male3,
            R.drawable.male4,
            R.drawable.male5,
            R.drawable.male6,
            R.drawable.male7,
            R.drawable.female1,
            R.drawable.female2,
            R.drawable.female3,
            R.drawable.female4,
            R.drawable.female5,
            R.drawable.female6,
            R.drawable.female7
    };

    private Context mContext;

    public ImageAdapter(Context c){
        mContext = c;
    }
    public ImageAdapter(){}

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}

