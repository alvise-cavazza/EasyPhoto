package com.example.alvise.provafoto;

/**
 * Created by alvis on 07/06/2017.
 */


import android.app.Dialog;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.widget.EditText;
import android.widget.MediaController;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.data;

public class AndroidImageAdapter extends PagerAdapter {
    Context mContext;
    private Bitmap[] sliderImagesId;
    private ArrayList <Uri> uris;
    private ArrayList <Integer> video= new ArrayList<Integer>();

    AndroidImageAdapter(Context context, ArrayList <Uri> uris, int conta, ArrayList <Integer> video) {
        this.mContext = context;
        Bitmap[] supporto= new Bitmap [uris.size()];
        this.video=video;
        this.uris=uris;

        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_black_24dp);
        for (int i=0; i<conta;i++){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uris.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            supporto[i] = bitmap;
        }
        sliderImagesId=supporto;

    }

    @Override
    public int getCount() {
        return sliderImagesId.length;
    }


    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        final int supportovideo=i;
        if (video.get(i).intValue() == 1) {
           /* final VideoView mVideoView = new VideoView(mContext);
            mVideoView.setMediaController(new MediaController(mContext));
            mVideoView.setVideoURI(uris.get(i));
            mVideoView.requestFocus();
            //mVideoView.start();
*/


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //   Bitmap bitmap = BitmapFactory.decodeFile("C:\\Users\\alvis\\AndroidStudioProjects\\ProvaFoto\\app\\src\\main\\res\\drawable\\ic_add_black_24dp.png", options);

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(mContext, uris.get(i));
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1); //unit in microsecond



            // Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_add_black_24dp);

            ImageView imageView = new ImageView(mContext);
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
            imageView.setImageBitmap(bitmap);


/*
            mVideoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog presentDialog = new Dialog(mContext,android.R.style.Theme);
                    presentDialog.setTitle("Video");
                    presentDialog.setContentView(R.layout.activity_video);
                    VideoView video = (VideoView) presentDialog.findViewById(R.id.video);
                    video.setMediaController(new MediaController(mContext));
                    video.setVideoURI(uris.get(supportovideo));
                    video.requestFocus();
                    video.start();
                    presentDialog.show();
                }
            });


            ((ViewPager) container).addView(imageView, 0);
                return imageView;
        }


        //caso dell'immagine
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageBitmap(sliderImagesId[i]);

     /*   mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }); */


        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }




}