package alserdar.casavoice.intro_vip;

/**
 * Created by ALAZIZY on 2/19/2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

enum AnimateState
{
    asRunning, asPause
}

class Rocket
{
    boolean sleep = true;

    private float energy, length, mx, my, gravity, ox, oy, x, y, t;
    private float vx[], vy[];
    private int patch, red, green, blue;
    private Random random;

    Rocket(int a, int b, int g)
    {
        mx = a;
        my = b;
        gravity = g;
    }

    void init(int e, int p, int l, long seed)
    {
        energy = e;
        patch = p + 20;
        length = l;

        random = new Random( seed );

        vx = new float[patch];
        vy = new float[patch];

        red = ( int )( random.nextFloat() * 128 ) + 128;
        blue = ( int )( random.nextFloat() * 128 ) + 128;
        green = ( int )( random.nextFloat() * 128 ) + 128;

        ox = ( random.nextFloat() * mx / 2 ) + mx / 4;
        oy = ( random.nextFloat() * my / 2 ) + my / 4;

        for( int i = 0; i < patch; ++i )
        {
            vx[i] = ( ( random.nextFloat() + random.nextFloat() / 2 ) * energy ) - energy / ( random.nextInt( 2 ) + 1 );
            vy[i] = ( ( random.nextFloat() + random.nextFloat() / 2 ) * energy * 7 / 8 ) - energy / ( random.nextInt( 5 ) + 4 );
        }
    }

    public void start()
    {
        t = 0;
        sleep = false;
    }

    public void doDraw( Canvas canvas, Paint paint )
    {
        if ( ! sleep )
        {
            if ( t < length )
            {
                int i, cr, cg, cb;
                double s;

                cr = ( int )( random.nextDouble() * 64 ) - 32 + red;
                cg = ( int )( random.nextDouble() * 64 ) - 32 + green;
                cb = ( int )( random.nextDouble() * 64 ) - 32 + blue;

                if ( cr >= 0 && cr <= 256 )
                    red = cr;
                if ( cg >= 0 && cg <= 256 )
                    green = cg;
                if ( cb >= 0 && cb <= 256 )
                    blue = cb;

                int _red = red == 256 ? 255 : red;
                int _green = green == 256 ? 255 : green;
                int _blue = blue == 256 ? 255 : blue;

                int color = Color.rgb( _red, _green, _blue );

                paint.setColor( color );

                for ( i = 0; i < patch; ++i )
                {
                    s = ( double )t / 100;
                    x = ( int )( vx[i] * s );
                    y = ( int )( vy[i] * s - gravity * s * s );

                    canvas.drawCircle( ox + x, oy - y, 2f, paint );
                }

                paint.setColor( Color.RED);

                for ( i = 0; i < patch; ++i )
                {
                    if ( t >= length / 2 )
                    {
                        for ( int j = 0; j < 2; ++j )
                        {
                            s = ( double ) ( ( t - length / 2 ) * 2 + j ) / 100;
                            x = ( int )( vx[i] * s );
                            y = ( int )( vy[i] * s - gravity * s * s );

                            canvas.drawCircle( ox + x, oy - y, 2f, paint );
                        }
                    }
                }

                ++t;
            }
            else
                sleep = true;
        }
    }
}

class Fireworks
{
    /**
     * Maximum number of rockets.
     */
    public int MaxRocketNumber = 9;
    /**
     * Controls "energy" of firwork explosion. Default value 850.
     */
    public int MaxRocketExplosionEnergy = 950;
    /**
     * Controls the density of the firework burst. Larger numbers give higher density.
     * Default value 90.
     */
    public int MaxRocketPatchNumber = 90;
    /**
     * Controls the radius of the firework burst. Larger numbers give larger radius.
     * Default value 68.
     */
    public int MaxRocketPatchLength = 68;

    /**
     * Controls gravity of the firework simulation.
     * Default value 400.
     */
    public int Gravity = 400;

    transient private Rocket rocket[];
    transient private boolean rocketsCreated = false;

    private int width;
    private int height;

    Fireworks( int width, int height )
    {
        this.width = width;
        this.height = height;
    }

    void createRockets()
    {
        rocketsCreated = true;

        Rocket tempRocket[] = new Rocket[MaxRocketNumber];

        for ( int i = 0; i < MaxRocketNumber; i++ )
            tempRocket[i] = new Rocket( width, height, Gravity );

        rocket = tempRocket;
    }

    public synchronized void reshape( int width, int height )
    {
        this.width = width;
        this.height = height;

        rocketsCreated = false;
    }

    public void doDraw(Canvas canvas, Paint paint)
    {
        canvas.drawColor(Color.WHITE);

        int i, e, p, l;
        long s;

        boolean sleep;

        if ( ! rocketsCreated )
        {
            createRockets();
        }

        if ( rocketsCreated )
        {
            sleep = true;

            for ( i = 0; i < MaxRocketNumber; i++ )
                sleep = sleep && rocket[i].sleep;

            for ( i = 0; i < MaxRocketNumber; ++i )
            {
                e = ( int )( Math.random() * MaxRocketExplosionEnergy * 3 / 4 ) + MaxRocketExplosionEnergy / 4 + 1;
                p = ( int )( Math.random() * MaxRocketPatchNumber * 3 / 4 ) + MaxRocketPatchNumber / 4 + 1;
                l = ( int )( Math.random() * MaxRocketPatchLength * 3 / 4 ) + MaxRocketPatchLength / 4 + 1;
                s = ( long )( Math.random() * 10000 );

                Rocket r = rocket[i];
                if ( r.sleep && Math.random() * MaxRocketNumber * l < 2 )
                {
                    r.init( e, p, l, s );
                    r.start();
                }

                if ( rocketsCreated )
                    r.doDraw( canvas, paint );
            }
        }
    }
}

public class FireworkView extends SurfaceView implements SurfaceHolder.Callback {

    class GameThread extends Thread {
        private boolean mRun = false;

        private final SurfaceHolder surfaceHolder;
        private AnimateState state;
        private Context context;
        private Handler handler;
        private Paint paint;
        Fireworks fireworks;

        GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            this.surfaceHolder = surfaceHolder;
            this.context = context;
            this.handler = handler;

            fireworks = new Fireworks(getWidth(), getHeight());

            paint = new Paint();
            paint.setStrokeWidth(2 / getResources().getDisplayMetrics().density);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
        }

        public void doStart() {
            synchronized (surfaceHolder) {
                setState(AnimateState.asRunning);
            }
        }

        public void pause() {
            synchronized (surfaceHolder) {
                if (state == AnimateState.asRunning)
                    setState(AnimateState.asPause);
            }
        }

        public void unpause() {
            setState(AnimateState.asRunning);
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);

                    synchronized (surfaceHolder) {
                        if (state == AnimateState.asRunning)
                            doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            mRun = b;
        }

        public void setState(AnimateState state) {
            synchronized (surfaceHolder) {
                this.state = state;
            }
        }

        public void doDraw(Canvas canvas) {
            fireworks.doDraw(canvas, paint);
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {
                fireworks.reshape(width, height);
            }
        }
    }

    private GameThread thread;

    @SuppressLint("HandlerLeak")
    public FireworkView(Context context) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        getHolder().addCallback(this);

        thread = new GameThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {

            }
        });

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.doStart();
        thread.start();
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3500);
                } catch (InterruptedException e) {
                    e.getMessage();
                } finally {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            thread.pause();
                        }
                    }).start();
                }
            }
        };
        timer.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}