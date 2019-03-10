package alserdar.casavoice.load_stuff;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.models.UserModel;

public class LoadUIForLiveRoom {


    public static void removeMute(Context context , String userName, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.stop_mute_for) + userName)
                .setMessage(context.getString(R.string.are_you_sure))
                .setPositiveButton(context.getString(R.string.ok), listener).setNegativeButton(context.getString(R.string.cancel), null).show();
    }

    public static void removeMuteDetails(TextView txtMic, View micDisable, View micMuted, View mic) {
        if (TextUtils.isEmpty(txtMic.getText())) {
            micButtonsControllersVisibility(micDisable, View.GONE, micMuted, View.GONE, mic, View.VISIBLE);
        } else {

            micButtonsControllersVisibility(micDisable, View.VISIBLE, micMuted, View.GONE, mic, View.GONE);
        }
    }

    public static void micButtonsControllersVisibility(View micDisable, int micDisableVisibility, View micMuted, int micMutedVisibility, View mic, int micVisibility) {
        micDisable.setVisibility(micDisableVisibility);
        micMuted.setVisibility(micMutedVisibility);
        mic.setVisibility(micVisibility);
    }

    public static void off(final Context context , final TextView txtMic, final View theGoneMic, final int theGoneMicVisibility, final View enableOne, final boolean enableOneBool
            , final View enableTwo, final boolean enableTwoBool, final View enableThree, final boolean enableThreeBool,
                           final View enableFour, final boolean enableFourBool, final View theVisMic, final int theVisMicVisibility)
    {

        new OurToast().myToast(context, "wait a moment to go Offline ...");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                theGoneMic.setVisibility(theGoneMicVisibility);
                enableOne.setEnabled(enableOneBool);
                enableTwo.setEnabled(enableTwoBool);
                enableThree.setEnabled(enableThreeBool);
                enableFour.setEnabled(enableFourBool);
                theVisMic.setVisibility(theVisMicVisibility);
                txtMic.setText("");
            }
        }, 10000);



    }


    public static void goOffOrMute(final Context context , final TextView txtMic, String userName, final View theGoneMic, final int theGoneMicVisibility, final View enableOne, final boolean enableOneBool
            , final View enableTwo, final boolean enableTwoBool, final View enableThree, final boolean enableThreeBool,
                             final View enableFour, final boolean enableFourBool, final View theVisMic, final int theVisMicVisibility
                              ,DialogInterface.OnClickListener listener) {
        if (txtMic.getText().toString().equals(userName)) {

            new OurToast().myToast(context , "wait a moment to go Offline ...");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    //micsDisabled(whichMicIsOff , offVisibility , whichMicIsGone , goneVisibility);
                    theGoneMic.setVisibility(theGoneMicVisibility);
                    enableOne.setEnabled(enableOneBool);
                    enableTwo.setEnabled(enableTwoBool);
                    enableThree.setEnabled(enableThreeBool);
                    enableFour.setEnabled(enableFourBool);
                    theVisMic.setVisibility(theVisMicVisibility);
                    txtMic.setText("");
                    //new LiveRoom().stopLiveMic();

                }
            }, 10000);


        } else {

            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.mute))
                    .setMessage(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.ok), listener).setNegativeButton(context.getString(R.string.cancel), null).show();
        }
    }




    public static void micsUI(View theGoneMic, int theGoneMicVisibility, View disableOne, boolean disableOneBool
            , View disableTwo, boolean disableTwoBool, View disableThree, boolean disableThreeBool,
                        View disableFour, boolean disableFourBool, View theVisMic, int theVisMicVisibility) {
        theGoneMic.setVisibility(theGoneMicVisibility);
        disableOne.setEnabled(disableOneBool);
        disableTwo.setEnabled(disableTwoBool);
        disableThree.setEnabled(disableThreeBool);
        disableFour.setEnabled(disableFourBool);
        theVisMic.setVisibility(theVisMicVisibility);
    }


    public static void faveThisRoomUI (Button faveThisRoom , int faveVis, Button unFaveThisRoom , int unfaveVis)
    {
        faveThisRoom.setVisibility(faveVis);
        unFaveThisRoom.setVisibility(unfaveVis);
    }

    public static void countUserForRichList(TextView onlineGuysInRichList , TextView onlineGuysWithRealNameInRichList , UserModel user)
    {

        onlineGuysInRichList.setText(user.getUserName());
        onlineGuysWithRealNameInRichList.setText(user.getTheUID());
    }
}
