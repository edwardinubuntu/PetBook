package com.edinubuntu.petlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.util.manager.DisplayTextManager;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class BadgesAdapter extends ArrayAdapter<Badge> {

    private List<Badge> badges;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public BadgesAdapter(Context context, int resource, List<Badge> objects) {
        super(context, resource, objects);

        this.badges = objects;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View contentView = convertView;
        if (contentView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.badge_cell, null);
        }

        Badge currentBadge = getBadges().get(position);

        ImageView imageView = (ImageView)contentView.findViewById(R.id.badge_image_view);
        imageView.setImageResource(R.drawable.ic_action_achievement);

        TextView textView = (TextView)contentView.findViewById(R.id.badge_text_view);
        textView.setText(DisplayTextManager.newInstance(getContext()).getBadgeText(currentBadge));

        return contentView;
    }
}
