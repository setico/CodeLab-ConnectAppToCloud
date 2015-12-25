package org.gdg_lome.codelab_connectapptocloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by setico on 25/12/2015.
 */
public class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> programmes;

    public ListAdapter(Context context, ArrayList<HashMap<String, String>> programmes) {
        this.context = context;
        this.programmes = programmes;
    }

    public static class ViewHolder{
        public ImageView logo;
        public TextView nom;
        public TextView description;
        public ProgressBar progress;

        public ViewHolder(View view) {
            logo = (ImageView) view.findViewById(R.id.logo);
            nom = (TextView) view.findViewById(R.id.nom);
            description = (TextView) view.findViewById(R.id.description);
            progress = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    @Override
    public int getCount() {
        return programmes.size();
    }

    @Override
    public Object getItem(int position) {
        return programmes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> programme = programmes.get(position);
        final ViewHolder viewHolder;
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items, null);
            viewHolder = new ViewHolder(convertView);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            viewHolder.nom.setText(programme.get(Config.NOM_KEY));
            viewHolder.description.setText(programme.get(Config.DESCRIPTION_KEY));
            Glide.with(context)
                    .load(programme.get(Config.LOGO_KEY))
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.progress.setVisibility(View.GONE);
                            return false;
                        }
                    }) .into(viewHolder.logo);

        convertView.setTag(viewHolder);
        return convertView;
    }
}
