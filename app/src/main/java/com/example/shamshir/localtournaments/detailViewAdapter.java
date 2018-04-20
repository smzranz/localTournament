package com.example.shamshir.localtournaments;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;


public class detailViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;

    //we are storing all the products in a list
    private Product productList;

    //getting the context and product list with constructor
    public detailViewAdapter(Context mCtx, Product productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                LayoutInflater inflater2 = LayoutInflater.from(mCtx);
                View view2 = inflater2.inflate(R.layout.imageviewlayout,parent,false);
                return new detailViewAdapter.ViewHolder3(view2);
//            case 1:
//                LayoutInflater inflater = LayoutInflater.from(mCtx);
//                View view = inflater.inflate(R.layout.list_layout,parent,false);
//                return new detailViewAdapter.ViewHolder0(view);

            case 1:

                LayoutInflater inflater1 = LayoutInflater.from(mCtx);
                View view1 = inflater1.inflate(R.layout.detail_card_view,parent,false);
                return new detailViewAdapter.ViewHolder2(view1);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Product product;



            product = productList;


        switch (holder.getItemViewType()) {
            case 0:

                ViewHolder3 viewHolder3 = (ViewHolder3)holder;
                Picasso.get().load(product.getImage()).placeholder(R.drawable.gameimage).into(viewHolder3.headerImageView);

                break;
//            case 1:
//                ViewHolder0 viewHolder0 = (ViewHolder0)holder;
//
//
//                viewHolder0.textViewTitle.setText(product.getTitle());
//                viewHolder0.textViewPlace.setText(product.getPlace());
//                viewHolder0.textViewComments.setText(product.getDate());
//                viewHolder0.textViewPrice.setText(product.getPrice());
//                viewHolder0.textViewSelectedGame.setText(product.getSelectedGame());
//                Picasso.get().load(product.getImage()).placeholder(R.drawable.gameimage).into(viewHolder0.imageView);
//                break;

            case 1:

                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                switch (position){

                    case 1:
                        viewHolder2.textTitle.setText("Title");
                        viewHolder2.textSubTitle.setText(product.getTitle());
                        break;
                    case 2:
                        viewHolder2.textTitle.setText("Game");
                        viewHolder2.textSubTitle.setText(product.getSelectedGame());
                        break;
                    case 3:
                        viewHolder2.textTitle.setText("Date");
                        viewHolder2.textSubTitle.setText(product.getDate());
                        break;
                    case 4:
                        viewHolder2.textTitle.setText("Location");
                        viewHolder2.textSubTitle.setText(product.getPlace());
                        break;
                    case 5:
                        viewHolder2.textTitle.setText("Registration Fee");
                        viewHolder2.textSubTitle.setText(product.getPrice());
                        break;
                    case 6:
                        viewHolder2.textTitle.setText("Contact");
                        viewHolder2.textSubTitle.setText(product.getContact());
                        break;
                    case 7:
                        viewHolder2.textTitle.setText("Total no of Teams");
                        viewHolder2.textSubTitle.setText(product.getTotalTeams());
                        break;
                    case 8:
                        viewHolder2.textTitle.setText("More");
                        viewHolder2.textSubTitle.setText(product.getMoreInfo());
                    break;
                }


                break;
        }


    }

    @Override
    public int getItemCount() {
        return 9;
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {


        TextView textViewTitle, textViewPlace, textViewComments, textViewPrice,textViewSelectedGame;
        ImageView imageView;

            public ViewHolder0(View itemView){
                super(itemView);
                textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
                textViewPlace = (TextView) itemView.findViewById(R.id.textViewShortDesc);
                textViewComments = (TextView)itemView.findViewById(R.id.textViewRating);
                textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
                textViewSelectedGame = (TextView) itemView.findViewById(R.id.textViewGameSelected);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);

            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder {

            TextView textTitle, textSubTitle;


            public ViewHolder2(View itemView) {
                super(itemView);
                textTitle = (TextView) itemView.findViewById(R.id.detailTitle);
                textSubTitle = (TextView) itemView.findViewById(R.id.detailSubTitle);


            }
        }


    class ViewHolder3 extends RecyclerView.ViewHolder {

        ImageView headerImageView;


        public ViewHolder3(View itemView) {
            super(itemView);
            headerImageView = (ImageView) itemView.findViewById(R.id.headerImageView);



        }
    }
            @Override
            public int getItemViewType(int position) {


        if (position == 0){

            return 0;

        }else  {


                    return 1;
                }

            }




        }
