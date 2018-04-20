package com.example.shamshir.localtournaments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



    public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mCtx;

        //we are storing all the products in a list
        private ProfileDetails profileDetails;

        //getting the context and product list with constructor
        public ProfileAdapter(Context mCtx, ProfileDetails profileDetails) {
            this.mCtx = mCtx;
            this.profileDetails = profileDetails;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case 0:
                    LayoutInflater inflater2 = LayoutInflater.from(mCtx);
                    View view2 = inflater2.inflate(R.layout.imageviewlayout,parent,false);
                    return new ProfileAdapter.ViewHolder3(view2);


                case 1:

                    LayoutInflater inflater1 = LayoutInflater.from(mCtx);
                    View view1 = inflater1.inflate(R.layout.detail_card_view,parent,false);
                    return new ProfileAdapter.ViewHolder2(view1);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Product product;






            switch (holder.getItemViewType()) {
                case 0:

                    ViewHolder3 viewHolder3 = (ViewHolder3)holder;
                   // Picasso.get().load(profileDetails.).placeholder(R.drawable.gameimage).into(viewHolder3.headerImageView);;


                    break;


                case 1:

                    ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                    switch (position){
                        case 0:
                            viewHolder2.textTitle.setText("Name");
                            viewHolder2.textSubTitle.setText(profileDetails.getName());
                            break;
                        case 1:
                            viewHolder2.textTitle.setText("Mobile number");
                            viewHolder2.textSubTitle.setText(profileDetails.getMobileNumber());
                            break;
                        case 2:
                            viewHolder2.textTitle.setText("Password");
                            viewHolder2.textSubTitle.setText(profileDetails.getPassword());
                            break;
                        case 3:
                            viewHolder2.textTitle.setText("Club Name");
                            viewHolder2.textSubTitle.setText(profileDetails.getClubName());
                            break;
                        case 4:
                            viewHolder2.textTitle.setText("My Tournaments");
                            viewHolder2.textSubTitle.setText("");
                            break;
                        case 5:
                            viewHolder2.textTitle.setText("Requests");
                            viewHolder2.textSubTitle.setText("");
                            break;
                        case 6:
                            viewHolder2.textTitle.setText("LogOut");
                            viewHolder2.textSubTitle.setText("");
                            break;

                    }


                    break;
            }


        }

        @Override
        public int getItemCount() {
            return 7;
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


//            if (position == 0){
//
//                return 0;
//
//            }else  {


                return 1;
         //   }

        }




    }


