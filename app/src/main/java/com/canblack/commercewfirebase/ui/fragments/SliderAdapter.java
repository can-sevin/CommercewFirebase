package com.canblack.commercewfirebase.ui.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.canblack.commercewfirebase.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    public Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        switch (position) {
            case 0:
                Picasso.get().load("https://images.pexels.com/photos/792199/pexels-photo-792199.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260").
                        into(viewHolder.imageViewBackground);
                break;
            case 1:
                Picasso.get().load("https://images.pexels.com/photos/2726370/pexels-photo-2726370.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260").
                        into(viewHolder.imageViewBackground);
                break;
            case 2:
                Picasso.get().load("https://images.pexels.com/photos/594452/pexels-photo-594452.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260").
                        into(viewHolder.imageViewBackground);
                break;
            default:
                Picasso.get().load("https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").
                        into(viewHolder.imageViewBackground);
                break;
        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 4;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        private SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
