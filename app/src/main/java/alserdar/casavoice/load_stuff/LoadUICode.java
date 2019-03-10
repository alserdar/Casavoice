package alserdar.casavoice.load_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.R;
import alserdar.casavoice.models.UserModel;

public class LoadUICode {




    public static void loadInfoForMyRoomUI
            (LinearLayout createRoomLayout , int createRoomLayoutVisibility ,
             LinearLayout myRoomLayout , int myRoomLayoutVisibility)
    {
        createRoomLayout.setVisibility(createRoomLayoutVisibility);
        myRoomLayout.setVisibility(myRoomLayoutVisibility);

    }

    public static void readUserInfo(Context context , UserModel user , TextView userNameProfileInfo ,
                                    TextView idProfileInfo , TextView levelProfileInfo , TextView genderProfileInfo ,
                                    TextView ageProfileInfo , TextView homerProfileInfo , TextView carProfileInfo ,
                                    TextView petProfileInfo , ImageView carPic , ImageView petPic)
    {
        userNameProfileInfo.setText(user.getUserName());
        idProfileInfo.setText(String.format("ID : %s", user.getLittleID()));
        levelProfileInfo.setText(String.format("Lv : %s", String.valueOf(user.getLevel())));
        genderProfileInfo.setText(String.format("%s %s", context.getString(R.string.gender), user.getGender()));
        ageProfileInfo.setText(String.format("%s %s", context.getString(R.string.birth_date), user.getBirthDate()));
        homerProfileInfo.setText(String.format("%s ", user.getHouse()));
        carProfileInfo.setText(String.format("%s ",  user.getCar()));
        petProfileInfo.setText(String.format("%s ",  user.getPet()));

        getCarPic(context , user.getCar() , carPic);
        getPetPic(context , user.getPet() , petPic);
    }

    public static void getPetPic(Context context , String pet , ImageView petPic)
    {
        switch (pet)
        {
            case "Cat":
                Bitmap cat = BitmapFactory.decodeResource(context.getResources() , R.mipmap.cat);
                petPic.setImageBitmap(cat);
                break;

            case "Dog":
                Bitmap dog = BitmapFactory.decodeResource(context.getResources() , R.mipmap.dog);
                petPic.setImageBitmap(dog);
                break;

            case "Rabbit":
                Bitmap rabbit = BitmapFactory.decodeResource(context.getResources() , R.mipmap.rabbit);
                petPic.setImageBitmap(rabbit);
                break;

            case "Dove":
                Bitmap dove = BitmapFactory.decodeResource(context.getResources() , R.mipmap.hmama);
                petPic.setImageBitmap(dove);
                break;

            case "Deer":
                Bitmap derr = BitmapFactory.decodeResource(context.getResources() , R.mipmap.ghazal);
                petPic.setImageBitmap(derr);
                break;

            case "Camel":
                Bitmap camel = BitmapFactory.decodeResource(context.getResources() , R.mipmap.camel);
                petPic.setImageBitmap(camel);
                break;

            case "Tiger":
                Bitmap tiger = BitmapFactory.decodeResource(context.getResources() , R.mipmap.tuger);
                petPic.setImageBitmap(tiger);
                break;
            case "Lion":
                Bitmap lion = BitmapFactory.decodeResource(context.getResources() , R.mipmap.lios);
                petPic.setImageBitmap(lion);
                break;

            case "Peacock":
                Bitmap tawoos = BitmapFactory.decodeResource(context.getResources() , R.mipmap.tawoos);
                petPic.setImageBitmap(tawoos);
                break;

            case "Horse":
                Bitmap horse = BitmapFactory.decodeResource(context.getResources() , R.mipmap.white_horse);
                petPic.setImageBitmap(horse);
                break;

            case "قطة":
                Bitmap catA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.cat);
                petPic.setImageBitmap(catA);
                break;

            case "كلب":
                Bitmap dogA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.dog);
                petPic.setImageBitmap(dogA);
                break;

            case "أرنب":
                Bitmap rabbitA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.rabbit);
                petPic.setImageBitmap(rabbitA);
                break;

            case "حمامة":
                Bitmap doveA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.hmama);
                petPic.setImageBitmap(doveA);
                break;

            case "غزال":
                Bitmap derrA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.ghazal);
                petPic.setImageBitmap(derrA);
                break;

            case "جمل":
                Bitmap camelA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.camel);
                petPic.setImageBitmap(camelA);
                break;

            case "نمر":
                Bitmap tigerA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.tuger);
                petPic.setImageBitmap(tigerA);
                break;
            case "أسد":
                Bitmap lionA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.lios);
                petPic.setImageBitmap(lionA);
                break;

            case "طاووس":
                Bitmap tawoosA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.tawoos);
                petPic.setImageBitmap(tawoosA);
                break;

            case "حصان":
                Bitmap horseA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.white_horse);
                petPic.setImageBitmap(horseA);
                break;
        }
    }

    public static void getCarPic(Context context , String car , ImageView carPic)
    {
        switch (car)
        {

            case "Fiat":
                Bitmap Fiat = BitmapFactory.decodeResource(context.getResources() , R.mipmap.fiat);
                carPic.setImageBitmap(Fiat);
                break;

            case "Totyota":
                Bitmap toyota = BitmapFactory.decodeResource(context.getResources() , R.mipmap.toyota);
                carPic.setImageBitmap(toyota);
                break;

            case "Mitsubishi":
                Bitmap mitsubishi = BitmapFactory.decodeResource(context.getResources() , R.mipmap.metsubishi);
                carPic.setImageBitmap(mitsubishi);
                break;
            case "Ford":
                Bitmap ford = BitmapFactory.decodeResource(context.getResources() , R.mipmap.ford);
                carPic.setImageBitmap(ford);
                break;
            case "Audi":
                Bitmap audi = BitmapFactory.decodeResource(context.getResources() , R.mipmap.audi);
                carPic.setImageBitmap(audi);
                break;
            case "BMW":
                Bitmap bmw = BitmapFactory.decodeResource(context.getResources() , R.mipmap.bmw);
                carPic.setImageBitmap(bmw);
                break;

            case "Mercedes":
                Bitmap mercedes = BitmapFactory.decodeResource(context.getResources() , R.mipmap.mercedes);
                carPic.setImageBitmap(mercedes);
                break;
            case "Jaguar":
                Bitmap jaguar = BitmapFactory.decodeResource(context.getResources() , R.mipmap.jaguar);
                carPic.setImageBitmap(jaguar);
                break;

            case "Cadillac":
                Bitmap cadillac = BitmapFactory.decodeResource(context.getResources() , R.mipmap.cadilac);
                carPic.setImageBitmap(cadillac);
                break;

            case "Ferrari":
                Bitmap ferrari = BitmapFactory.decodeResource(context.getResources() , R.mipmap.ferrarii);
                carPic.setImageBitmap(ferrari);
                break;

            case "Rolls royce":
                Bitmap rolsroys = BitmapFactory.decodeResource(context.getResources() , R.mipmap.rolsrois);
                carPic.setImageBitmap(rolsroys);
                break;

            case "فيات":
                Bitmap FiatA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.fiat);
                carPic.setImageBitmap(FiatA);
                break;

            case "تويوتا":
                Bitmap toyotaA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.toyota);
                carPic.setImageBitmap(toyotaA);
                break;

            case "ميتسوبيشي":
                Bitmap mitsubishiA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.metsubishi);
                carPic.setImageBitmap(mitsubishiA);
                break;
            case "فورد":
                Bitmap fordA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.ford);
                carPic.setImageBitmap(fordA);
                break;
            case "أودي":
                Bitmap audiA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.audi);
                carPic.setImageBitmap(audiA);
                break;
            case "بي ام دبليو":
                Bitmap bmwA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.bmw);
                carPic.setImageBitmap(bmwA);
                break;

            case "ميرسيدس":
                Bitmap mercedesA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.mercedes);
                carPic.setImageBitmap(mercedesA);
                break;
            case "جاكوار":
                Bitmap jaguarA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.jaguar);
                carPic.setImageBitmap(jaguarA);
                break;

            case "كاديلاك":
                Bitmap cadillacA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.cadilac);
                carPic.setImageBitmap(cadillacA);
                break;

            case "فيراري":
                Bitmap ferrariA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.ferrarii);
                carPic.setImageBitmap(ferrariA);
                break;

            case "رولزس رويس":
                Bitmap rolsroysA = BitmapFactory.decodeResource(context.getResources() , R.mipmap.rolsrois);
                carPic.setImageBitmap(rolsroysA);
                break;
        }
    }

    public static void readUserInfoForFollowingList
            (Context context , Button follow  , int followVisibility,
             Button unfollow  , int unfollowVisibility , Button block , boolean checkBlock , TextView txtFollow , int txtFollowResource)
    {
        follow.setVisibility(followVisibility);
        unfollow.setVisibility(unfollowVisibility);
        block.setEnabled(checkBlock);
        txtFollow.setText(context.getString(txtFollowResource));
    }

    public static void readUserForMyBlockList(Context context , Button chat , boolean checkChat , Button follow , boolean checkFollow ,
                                               TextView txtBlock , int txtBlockResource , int color)
    {
        chat.setEnabled(checkChat);
        follow.setEnabled(checkFollow);
        txtBlock.setText(context.getString(txtBlockResource));
        txtBlock.setTextColor(color);
    }

    public static void readUserForHisBlockList(Button chat , boolean checkChat , Button follow , boolean checkFollow)
    {
        chat.setEnabled(checkChat);
        follow.setEnabled(checkFollow);
    }

    public static void followFunUI(Context context , Button follow , int followVisibility ,
                                   Button unfollow , int unfollowVisibility , TextView txtFollow , int unfollowResource)
    {
        follow.setVisibility(followVisibility);
        unfollow.setVisibility(unfollowVisibility);
        txtFollow.setText(context.getString(unfollowResource));
    }

}
