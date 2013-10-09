package com.edinubuntu.petlove.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.MarketSource;
import com.squareup.picasso.Picasso;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class MarketSourceAdapter extends ArrayAdapter<MarketSource> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public MarketSourceAdapter(Context context, int resource, MarketSource[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View contentView = convertView;
        if (contentView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.market_source_cell, null);
        }

        MarketSource marketSource = getItem(position);

        ImageView imageView = (ImageView) contentView.findViewById(R.id.market_source_image_view);
        TextView textView = (TextView) contentView.findViewById(R.id.market_source_text_view);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams imageViewLayoutParams = imageView.getLayoutParams();
        imageViewLayoutParams.height = metrics.widthPixels / 4;

        Picasso.with(getContext()).
                load(marketSource.getImageURL())
                .into(imageView);

        textView.setText(marketSource.getTitle());

        return contentView;
    }
}
