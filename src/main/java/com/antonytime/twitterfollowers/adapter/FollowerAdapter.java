package com.antonytime.twitterfollowers.adapter;

import android.content.Context;
import android.graphics.*;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.antonytime.twitterfollowers.Follower;
import com.antonytime.twitterfollowers.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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

        ImageView imageView = (ImageView) view.findViewById(R.id.prof_image);
        Bitmap bitmap = getBitmap(follower.getId());
        imageView.setImageBitmap(bitmap);

        return view;
    }

    private Follower getFollower(int position){
        return (Follower) getItem(position);
    }

    public Bitmap getBitmap(long imageId) {

        FileInputStream in;
        BufferedInputStream buf;
        Bitmap bMap = null;

        try {
            String root = Environment.getExternalStorageDirectory().toString();
            in = new FileInputStream(root + "/TwitterFollowers/saved_profiles_images/Image-"+ imageId + ".jpg");
            buf = new BufferedInputStream(in);
            bMap = BitmapFactory.decodeStream(buf);

            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }
        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }

        return circleImage(bMap);
    }

    public Bitmap circleImage(Bitmap bitmap){
        Bitmap image_circle = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(image_circle);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        return image_circle;
    }

}
