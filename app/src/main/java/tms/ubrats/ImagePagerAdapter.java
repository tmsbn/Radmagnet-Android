
package tms.ubrats;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * ImagePagerAdapter
 * 
 *
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context       context;
    private List<Integer> imageIdList;

    public ImagePagerAdapter(Context context, List<Integer> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
    }

    @Override
    public int getCount() {
          return imageIdList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageIdList.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        (container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((ImageView)object);
    }
}