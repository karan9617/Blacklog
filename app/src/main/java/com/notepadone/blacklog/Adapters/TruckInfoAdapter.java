package com.notepadone.blacklog.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.notepadone.blacklog.Objects.TrucksObject;
import com.notepadone.blacklog.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Belal on 10/18/2017.
 */


public class TruckInfoAdapter extends RecyclerView.Adapter<TruckInfoAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    TextView address;
    //we are storing all the products in a list
    private List<TrucksObject> productList;

    //getting the context and product list with constructor
    public TruckInfoAdapter(Context mCtx, List<TrucksObject> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_trucks, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        TrucksObject product = productList.get(position);

        //binding the data with the viewholder views
        holder.signal.setText(product.getSignal());
        holder.blacklogBasic.setText(product.getBasic());
        holder.lastUpdate.setText(String.valueOf(product.getFuelLid()));
        holder.fuel.setText(String.valueOf(product.getLastUpdate()));
        holder.speed.setText(String.valueOf(product.getSpeed()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView signal, blacklogBasic, lastUpdate, fuel,speed;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            signal = itemView.findViewById(R.id.signal);
            speed = itemView.findViewById(R.id.speed);
            blacklogBasic = itemView.findViewById(R.id.blacklogBasic);
            lastUpdate = itemView.findViewById(R.id.lastUpdate);
            fuel = itemView.findViewById(R.id.fuel);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}