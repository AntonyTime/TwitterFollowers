package com.antonytime.twitterfollowers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.antonytime.twitterfollowers.Follower;
import com.antonytime.twitterfollowers.R;

import java.util.ArrayList;
import java.util.List;

public class FollowerAdapter extends BaseAdapter{

    private List<Follower> list;
    private LayoutInflater layoutInflater;

    public FollowerAdapter(Context context, ArrayList<Follower> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = layoutInflater.inflate(R.layout.item_layout, parent, false);
        }

        Follower follower = getFollower(position);

        TextView textViewName = (TextView) view.findViewById(R.id.name);
        String name = follower.getName();
        textViewName.setText(name);

        TextView textViewId = (TextView) view.findViewById(R.id.id);
        String id = String.valueOf(follower.getId());
        textViewId.setText(id);

        return view;
    }

    private Follower getFollower(int position){
        return (Follower) getItem(position);
    }
}
