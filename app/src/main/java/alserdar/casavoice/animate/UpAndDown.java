package alserdar.casavoice.animate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ALAZIZY on 12/31/2017.
 */

public class UpAndDown {

    Context context ;

    public void showTheLay(LinearLayout layout)
    {
        AnimatorSet animatorSet = new AnimatorSet();
        layout.setVisibility(View.VISIBLE);

        animatorSet.playTogether(
                ObjectAnimator.ofFloat(layout,"translationY",-1000,0),
                ObjectAnimator.ofFloat(layout,"alpha",0,1),
                ObjectAnimator.ofFloat(layout,"translationX",0,0)
        );
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    public void hideTheLay(LinearLayout layout)
    {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(layout,"translationY", 0 , -1000),
                ObjectAnimator.ofFloat(layout,"alpha",1,0),
                ObjectAnimator.ofFloat(layout,"translationX",0,0)
        );
        animatorSet.setDuration(1000);
        animatorSet.start();

    }

    public void introVip(LinearLayout layout)
    {
        AnimatorSet animatorSet = new AnimatorSet();
        layout.setVisibility(View.VISIBLE);

        animatorSet.playTogether(
                ObjectAnimator.ofFloat(layout,"translationY",-800,1500),
                ObjectAnimator.ofFloat(layout,"alpha",1),
                ObjectAnimator.ofFloat(layout,"translationX",0,0)
        );
        animatorSet.setDuration(4000);
        animatorSet.start();
    }
}
