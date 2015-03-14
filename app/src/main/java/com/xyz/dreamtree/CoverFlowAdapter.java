package com.xyz.dreamtree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CoverFlowAdapter extends BaseAdapter {
	
	private ArrayList<DreamEntity> mData = new ArrayList<>(0);
	private Context mContext;

	public CoverFlowAdapter(Context context) {
		mContext = context;
	}
	
	public void setData(ArrayList<DreamEntity> data) {
		mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int pos) {
		return mData.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_coverflow, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.data = (TextView) rowView.findViewById(R.id.data);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.image);
            viewHolder.date = (TextView) rowView.findViewById(R.id.date);
            viewHolder.time = (TextView) rowView.findViewById(R.id.time);
            viewHolder.mood = (TextView) rowView.findViewById(R.id.mood);


            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(mData.get(position).imageResId, 0, mData.get(position).imageResId.length));
        holder.date.setText(mData.get(position).date);
        holder.time.setText(mData.get(position).time);
        holder.mood.setText(mData.get(position).mood);
        holder.data.setText(mData.get(position).data);


        return rowView;
	}


    static class ViewHolder {
        public TextView date;
        public ImageView image;
        public TextView time;
        public TextView mood ;
        public TextView data;
    }
}
