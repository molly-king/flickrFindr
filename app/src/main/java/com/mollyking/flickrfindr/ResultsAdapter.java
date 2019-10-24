package com.mollyking.flickrfindr;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsHolder> {

    public ResultsAdapter(MainViewModel viewModel) {
        super();
        this.viewModel = viewModel;
    }

    private MainViewModel viewModel;

    private List<FlickrPhoto> photos = new ArrayList<>();


    @NonNull
    @Override
    public ResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsHolder holder, int position) {
        holder.bindData(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<FlickrPhoto> newPhotos) {
        photos = newPhotos;
        notifyDataSetChanged();
    }

    class ResultsHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView imageView;
        private TextView titleView;

        public ResultsHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_result);
            titleView = itemView.findViewById(R.id.title_result);
        }

        public void bindData(final FlickrPhoto photo) {
            imageView.setImageURI(Uri.parse(photo.getThumbnailUrl()));
            titleView.setText(photo.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.selectItem(photo);
                }
            });
        }
    }
}
