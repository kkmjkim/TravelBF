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

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private ArrayList<PlaceItem> arrayList;
    // act마다 context가 있는데, adapter에서 act의 action을 가져올 때 context가 필요한데
    // ad에서는 당장 없으니까 만들어줌
    // 선택한 act에 대한 context를 가져올 때 얘가 필요함
    private Context context;

    public PlaceAdapter(ArrayList<PlaceItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // ViewHolder 객체 생성: send views to RecyclerView, not to display items
    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // from(): Obtains the LayoutInflater from the given context
        // public View inflate (int resource, ViewGroup root, boolean attachToRoot)
        // Inflate a new view hierarchy from the resource
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_custom, parent, false);
        PlaceViewHolder holder = new PlaceViewHolder(view);
        return holder;
    } //review_recycler_custom에 대한 게 생성됨

    //Called by RecyclerView to display the data at the specified position
    //매칭할 때 textview는 쉬운데 imageview는 서버에서 url로 받아와야 하니까 gradle에 외부 라이브러리 추가
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        //Glide.with(holder.itemView).load(arrayList.get(position).getImage()).into(holder.iv_image);
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_addr.setText(arrayList.get(position).getAddr());
    }


    @Override
    public int getItemCount() {return (arrayList != null ? arrayList.size() : 0);}

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_addr;


        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            //find 바로 X, RecyclerView.ViewHolder 상속받은거라서 그친구의 id
            //this.iv_image =itemView.findViewById(R.id.iv_image);
            this.tv_title =itemView.findViewById(R.id.tv_title);
            this.tv_addr =itemView.findViewById(R.id.tv_addr);

        }
    }

}
