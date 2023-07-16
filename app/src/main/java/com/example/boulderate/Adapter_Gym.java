package com.example.boulderate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import java.util.List;

public class Adapter_Gym extends RecyclerView.Adapter<Adapter_Gym.GymViewHolder> {
    private List<Gym> gyms;
    private OnGymClickListener onGymClickListener;
    public Adapter_Gym(List<Gym> gyms) {
        this.gyms = gyms;
    }
    public void updateList(List<Gym> Gyms) {
        this.gyms = gyms;
        notifyDataSetChanged();
    }

    public void setOnGymClickListener(OnGymClickListener onGymClickListener) {
        this.onGymClickListener = onGymClickListener;
    }

    @NonNull
    @Override
    public GymViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_gym_square, viewGroup, false);
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder holder, int position) {
        Gym gym = getItem(position);
        holder.gym_LBL_title.setText(gym.getTitle());
        holder.gym_LBL_rating.setText("Rating: " + gym.getRating());
        holder.gym_RTG_rating.setRating((float) gym.getRating());
        Imager.me().imageCrop(holder.gym_IMG_main, gym.getImage());
    }

    @Override
    public int getItemCount() {
        return gyms == null ? 0 : gyms.size();
    }

    Gym getItem(int position) {
        return gyms.get(position);
    }

    public interface OnGymClickListener {
        void onClick(View view, Gym gym, int position);
    }

    public class GymViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView gym_IMG_main;
        private MaterialTextView gym_LBL_title;
        private MaterialTextView gym_LBL_rating;
        private AppCompatRatingBar gym_RTG_rating;

        GymViewHolder(View itemView) {
            super(itemView);
            gym_IMG_main = itemView.findViewById(R.id.gym_IMG_main);
            gym_LBL_title = itemView.findViewById(R.id.gym_LBL_title);
            gym_LBL_rating = itemView.findViewById(R.id.gym_LBL_rating);
            gym_RTG_rating = itemView.findViewById(R.id.gym_RTG_rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGymClickListener.onClick(v, getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}