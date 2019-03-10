package alserdar.casavoice.load_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import alserdar.casavoice.R;

public class LoadingPics {

    public static void loadPicsForTopMenu(final Context context , final String theUID , final ImageView pic)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ theUID);
        Glide.with(context.getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .asBitmap()
                .placeholder(R.mipmap.my_icon)
                .into(new BitmapImageViewTarget(pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, 80, 80, false));
                        drawable.setCircular(true);
                        pic.setImageDrawable(drawable);
                    }
                });

    }

    static   public void loadPicsForHome (final Context context , final String theUID , final ImageView pic)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ theUID);
        Glide.with(context.getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .asBitmap()
                .placeholder(R.mipmap.my_icon)
                .into(new BitmapImageViewTarget(pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, 100, 100, false));
                        drawable.setCircular(true);
                        pic.setImageDrawable(drawable);
                    }
                });
    }

    public static void loadPicForRooms(final Context context , final String theUID , final ImageView pic)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("roomImages/"+ theUID);
        Glide.with(context.getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .asBitmap()
                .placeholder(R.mipmap.my_icon)
                .into(new BitmapImageViewTarget(pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, 150, 150, false));
                        drawable.setCircular(true);
                        pic.setImageDrawable(drawable);
                    }
                });
    }

    public void loadPicForVip(final Context context , final String userName , final ImageView pic)
    {
        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("vip/"+ "vip_platinum.png");
                Glide.with(context.getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(pic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                        Bitmap.createScaledBitmap(resource, 150, 150, false));
                                drawable.setCircular(true);
                                pic.setImageDrawable(drawable);
                            }
                        });
            }
        };
        mainHandler.post(myRunnable);
    }
}
