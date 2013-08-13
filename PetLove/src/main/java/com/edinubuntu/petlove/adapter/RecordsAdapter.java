package com.edinubuntu.petlove.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Record;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by edward_chiang on 13/8/12.
 */
public class RecordsAdapter extends ArrayAdapter<Record> {

    protected LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public RecordsAdapter(Context context, int resource, List<Record> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View contentView = convertView;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.pet_grid_cell, null);
        }

        Log.d(PetLove.TAG, "get view get width:" + contentView.getWidth() + ", height:" + contentView.getHeight());

        Record record = this.getItem(position);

        ImageView petImageView = (ImageView)contentView.findViewById(R.id.pet_image_view);

        int height;
        if (contentView.getWidth() == 0) {
            height = petImageView.getHeight();
        } else {
            height = (int)(contentView.getWidth() * PetLove.IMAGE_VIEW_HEIGHT_SCALE_RATE);
        }

        petImageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                height));

        petImageView.setImageResource(R.drawable.ic_card);
        ImageLoader.getInstance().displayImage(record.getImageName(), petImageView);

        TextView petNameTextView = (TextView)contentView.findViewById(R.id.pet_name_text_view);
        petNameTextView.setText(record.getName());

        TextView petDescriptionTextView = (TextView)contentView.findViewById(R.id.pet_note_text_view);
        petDescriptionTextView.setText(record.getNote());

        return contentView;
    }
}
