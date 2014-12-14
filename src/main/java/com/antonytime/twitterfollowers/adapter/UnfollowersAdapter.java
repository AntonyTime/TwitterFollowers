package com.antonytime.twitterfollowers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.pojo.Followers;

import java.util.List;

public class UnfollowersAdapter extends BaseAdapter {

    private List<Followers> list;
    private LayoutInflater layoutInflater;

    public UnfollowersAdapter(Context context, List<Followers> list) {
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

        Followers unfollowers = getUnfollowerName(position);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(unfollowers.getName());

        return view;
    }

    private Followers getUnfollowerName(int position){
        return (Followers) getItem(position);
    }

}
