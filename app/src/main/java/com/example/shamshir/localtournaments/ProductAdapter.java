package com.example.shamshir.localtournaments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;




public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder


        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.textViewTitle.setText(product.getTitle());
        holder.textViewPlace.setText(product.getPlace());
        holder.textViewComments.setText(product.getDate());
        holder.textViewPrice.setText(product.getPrice());
        holder.textViewSelectedGame.setText(product.getSelectedGame());
        Picasso.get().load(product.getImage()).placeholder(R.drawable.gameimage).into(holder.imageView);


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewPlace, textViewComments, textViewPrice,textViewSelectedGame;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewPlace = (TextView) itemView.findViewById(R.id.textViewShortDesc);
            textViewComments = (TextView)itemView.findViewById(R.id.textViewRating);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textViewSelectedGame = (TextView) itemView.findViewById(R.id.textViewGameSelected);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
