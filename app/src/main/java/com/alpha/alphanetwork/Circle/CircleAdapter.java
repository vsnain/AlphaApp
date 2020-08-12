package com.alpha.alphanetwork.Circle;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.alpha.alphanetwork.Feed.MediaAdapter;
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import android.support.v4.view.PagerAdapter;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import com.alpha.alphanetwork.Model.ModelFeed;
import com.alpha.alphanetwork.Profile.ProfileActivity;
import com.alpha.alphanetwork.R;
import Utils.Utils;
import Utils.LikesToggle;



public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.MyViewHolder>{

    private List<ModelFeed> posts;
    private Context context;
    private FragmentManager fragmentManager;
    private static final String TAG = "Adapter";






    public CircleAdapter(List<ModelFeed> posts, Context context,FragmentManager fragmentManager) {
        this.posts = posts;
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_feeditem, parent, false);
        return new MyViewHolder(view);
//        , onItemClickListener
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        final ModelFeed modelFeed = posts.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();






//        final PagerAdapter receive = addData(position);
//        if(receive == null)
//        {
////             View namebar = View.findViewById(R.id.namebar);
//            ((ViewGroup) holder.vp.getParent()).removeView(holder.vp);
//        }
//        else
//        {
//
//
//            holder.vp.setId(position+1);
//            holder.vp.setAdapter(receive);
//
//        }
        String t = posts.get(position).getVideo();

        if(!t.equals("")){
            ((ViewGroup) holder.vp.getParent()).removeView(holder.vp);
            holder.mVideo.setVisibility(View.VISIBLE);
            VideoControls controls = holder.mVideo.getVideoControls();
            controls.hide();
            holder.play.setVisibility(View.VISIBLE);
            holder.mVideo.setScaleType(ScaleType.CENTER_CROP);
            holder.mVideo.setMeasureBasedOnAspectRatioEnabled(false);

//            holder.mVideo.setOnPreparedListener(this);
        }

        else {


            final PagerAdapter receive = addData(position);
            if (receive == null) {
//             View namebar = View.findViewById(R.id.namebar);
                ((ViewGroup) holder.vp.getParent()).removeView(holder.vp);
            } else {


                holder.vp.setId(position + 1);
                holder.vp.setAdapter(receive);

            }
        }
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoControls controls = holder.mVideo.getVideoControls();
                controls.show();
                holder.mVideo.setVideoURI(Uri.parse(t));
                holder.mVideo.start();
                holder.play.setVisibility(View.GONE);
            }
        });






        holder.imgView_proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);

                intent.putExtra("user_id", modelFeed.getId()) ;
                context.startActivity(intent);

            }
        });


        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id", modelFeed.getId()) ;
                context.startActivity(intent);

            }
        });












        holder.tl.setupWithViewPager(holder.vp, true);


        holder.tv_name.setText(modelFeed.getProfile().getUser());
        holder.tv_time.setText(Utils.DateToTimeFormat(modelFeed.getTime()));
        holder.tv_likes.setText(String.valueOf(modelFeed.getLikes()));
        holder.tv_comments.setText(String.valueOf(modelFeed.getComments()));
        holder.tv_status.setText(modelFeed.getTitle());
        holder.tv_position.setText(modelFeed.getProfile().getPosition());

        Glide.with(context).load(modelFeed.getProfile().getPhoto()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgView_proPic);


//        if (modelFeed.getProfile().getPhoto() == null) {
//            holder.imgView_postPic.setVisibility(View.GONE);
//        } else {
//            holder.imgView_postPic.setVisibility(View.VISIBLE);
//            Glide.with(context).load(modelFeed.getProfile().getPhoto()).into(holder.imgView_postPic);
//        }

    }

    @Override
    public int getItemCount() {

        return posts.size();

    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
//        this.onItemClickListener = onItemClickListener;
//    }





    public PagerAdapter addData(int i)
    {
//        System.out.println(i);
//        System.out.println(posts.size());


        ModelFeed modelFeed = posts.get(i);
        List<String> urls = modelFeed.getMedia();



        String video = modelFeed.getVideo();
        String type = "media";
        if(!video.equals("")) {
            urls.clear();
            urls.add(modelFeed.getVideo());
            type = "video";
        }




        if(urls.size() == 0)
        {
            return null;
        }




        PagerAdapter pagerAdapter = new PagerAdapter(fragmentManager);
//        ArrayList<String> urls = modelFeed.getMedia().get(i);
        MediaAdapter one;

        for(int j = 0; j < urls.size(); j++)
        {
//            System.out.println(urls.get(j));
            one = MediaAdapter.newInstance(urls.get(j),context,type);
            pagerAdapter.mediaFragments.add(one);

        }
//        viewPager.setAdapter(pagerAdapter);
        return  pagerAdapter;
    }


    public class PagerAdapter extends FragmentStatePagerAdapter
    {
        //        public ArrayList<MediaFragment> arrayList =
        public ArrayList<MediaAdapter> mediaFragments = new ArrayList<>();
        public PagerAdapter(FragmentManager manager)
        {
            super(manager);
        }


        @Override
        public Fragment getItem(int i) {
            return mediaFragments.get(i);
        }

        @Override
        public int getCount() {
            return mediaFragments.size();
        }
    }






// implements  View.OnClickListener

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_time, tv_likes, tv_comments, tv_status,tv_position;
        ImageView imgView_proPic, imgView_postPic, imgView_comments, imgView_back;
        ProgressBar progressBar;
        ViewPager vp;
        TabLayout tl;
        ImageView imgView_like, imgView_liked, imgView_dislike, imgView_disliked, play;
        public VideoView mVideo;
        private GestureDetector lGestureDetector;
        private GestureDetector dGestureDetector;
        private LikesToggle like;





//, OnItemClickListener onItemClickListener

        public MyViewHolder(View itemView) {

            super(itemView);

//            itemView.setOnClickListener(this);
            imgView_proPic = (ImageView) itemView.findViewById(R.id.imgView_proPic);
//            imgView_postPic = (ImageView) itemView.findViewById(R.id.imgView_postPic);
            vp = itemView.findViewById(R.id.view_pager_media);
            tl = itemView.findViewById(R.id.tab_layout);
            tv_position = itemView.findViewById(R.id.tv_position);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_likes = (TextView) itemView.findViewById(R.id.tv_likescount);


            tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);
            imgView_comments = itemView.findViewById(R.id.speech_bubble);
            imgView_back = itemView.findViewById(R.id.backArrow);

            tv_status = (TextView) itemView.findViewById(R.id.tv_status);


            //likes toggling
            imgView_like = (ImageView) itemView.findViewById(R.id.image_like);
            imgView_liked= (ImageView) itemView.findViewById(R.id.image_liked);
            imgView_dislike = (ImageView) itemView.findViewById(R.id.image_dislike);
            imgView_disliked = (ImageView) itemView.findViewById(R.id.image_disliked);
            lGestureDetector = new GestureDetector(context,new lGestureListener());
            dGestureDetector = new GestureDetector(context,new dGestureListener());


            imgView_liked.setVisibility(View.GONE);
            imgView_like.setVisibility(View.VISIBLE);
            imgView_disliked.setVisibility(View.GONE);
            imgView_dislike.setVisibility(View.VISIBLE);

            mVideo = itemView.findViewById(R.id.videoplayervp);
            play = itemView.findViewById(R.id.startvideo);





            like = new LikesToggle(imgView_like,imgView_liked,imgView_dislike,imgView_disliked);
            likeToggle();
            dislikeToggle();




            //comments

            tv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String post = posts.get(position).getPostid();
                    (( Circle)context).onCommentThreadSelected(post,"comment");

                    //going to need to do something else?
                    ((Circle)context).hideLayout();

                }
            });


            imgView_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String post = posts.get(position).getPostid();
                    ((Circle)context).onCommentThreadSelected(post,"comment");
                    //going to need to do something else?
                    ((Circle)context).hideLayout();
                }
            });








//            this.onItemClickListener = onItemClickListener;

        }

//        @Override
//        public void onClick(View v) {
//            onItemClickListener.onItemClick(v, getAdapterPosition());
//        }




        private void likeToggle(){

            imgView_liked.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return lGestureDetector.onTouchEvent(event);
                }
            });


            imgView_like.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "Entered like touch listerner");
                    return lGestureDetector.onTouchEvent(event);
                }
            });


        }



        private void dislikeToggle(){
            imgView_disliked.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return dGestureDetector.onTouchEvent(event);
                }
            });


            imgView_dislike.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "Entered dislike touch listerner");
                    return dGestureDetector.onTouchEvent(event);
                }
            });
        }



        public class lGestureListener extends GestureDetector.SimpleOnGestureListener{



            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = getAdapterPosition();
                String id = posts.get(position).getPostid();


                like.toggleLike(id,"post");
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                int position = getAdapterPosition();
                String id = posts.get(position).getPostid();


                like.toggleLike(id,"post");
                return true;
            }




        }

        public class dGestureListener extends GestureDetector.SimpleOnGestureListener{

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = getAdapterPosition();
                String id = posts.get(position).getPostid();

                like.toggleDisLike(id,"post");
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                int position = getAdapterPosition();
                String id = posts.get(position).getPostid();

                like.toggleDisLike(id,"post");
                return true;
            }


        }



    }


}
