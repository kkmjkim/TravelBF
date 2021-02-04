package com.example.finaltbf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<ReviewItem> arrayList;
    // act마다 context가 있는데, adapter에서 act의 action을 가져올 때 context가 필요한데
    // ad에서는 당장 없으니까 만들어줌
    // 선택한 act에 대한 context를 가져올 때 얘가 필요함
    private Context context;

    public ReviewAdapter(ArrayList<ReviewItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // ViewHolder 객체 생성: send views to RecyclerView, not to display items
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // from(): Obtains the LayoutInflater from the given context
        // public View inflate (int resource, ViewGroup root, boolean attachToRoot)
        // Inflate a new view hierarchy from the resource
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_custom, parent, false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    } //review_recycler_custom에 대한 게 생성됨

    //Called by RecyclerView to display the data at the specified position
    //매칭할 때 textview는 쉬운데 imageview는 서버에서 url로 받아와야 하니까 gradle에 외부 라이브러리 추가
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        //Glide.with(holder.itemView).load(arrayList.get(position).getImage()).into(holder.iv_image);
        holder.tv_userName.setText(arrayList.get(position).getUserName());
        //holder.tv_rate.setText(String.valueOf(arrayList.get(position).getRate()));
        //holder.tv_date.setText(String.valueOf(arrayList.get(position).getDate()));
        holder.tv_review.setText(arrayList.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tv_userName;
        //ImageView iv_image;
        //TextView tv_rate;
        //TextView tv_date;
        TextView tv_review;


        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            //find 바로 X, RecyclerView.ViewHolder 상속받은거라서 그친구의 id
            //this.iv_image =itemView.findViewById(R.id.iv_image);
            this.tv_userName =itemView.findViewById(R.id.tv_userName);
            //this.tv_rate =itemView.findViewById(R.id.tv_rate);
            //this.tv_date =itemView.findViewById(R.id.tv_date);
            this.tv_review =itemView.findViewById(R.id.tv_review);

        }
    }

}
