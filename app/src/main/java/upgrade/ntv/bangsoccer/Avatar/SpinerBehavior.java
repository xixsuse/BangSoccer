package upgrade.ntv.bangsoccer.Avatar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;

import upgrade.ntv.bangsoccer.R;

@SuppressWarnings("unused")
public class SpinerBehavior extends CoordinatorLayout.Behavior<Spinner> {

    private final static float MIN_AVATAR_PERCENTAGE_SIZE   = 0.4f;
    private final static int EXTRA_FINAL_AVATAR_PADDING     = 80;

    private final static String TAG = "behavior1";
    private Context mContext;

    private float mCustomFinalYPosition;
    private float mCustomStartXPosition;
    private float mCustomStartToolbarPosition;
    private float mCustomStartHeight;
    private float mCustomFinalHeight;

    private float mAvatarMaxSize;
    private float mFinalLeftAvatarPadding;
    private float mStartPosition;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;

    public SpinerBehavior(Context context, AttributeSet attrs) {
        mContext = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinerBehavior);
            mCustomFinalYPosition = a.getDimension(R.styleable.SpinerBehavior_finalYPositionSpinner, 0);
            mCustomStartXPosition = a.getDimension(R.styleable.SpinerBehavior_startXPositionSpinner, 0);
            mCustomStartToolbarPosition = a.getDimension(R.styleable.SpinerBehavior_startToolbarPositionSpinner, 0);
            mCustomStartHeight = a.getDimension(R.styleable.SpinerBehavior_startHeightSpinner, 0);
            mCustomFinalHeight = a.getDimension(R.styleable.SpinerBehavior_finalHeightSpinner, 0);

            a.recycle();
        }

        init();

        mFinalLeftAvatarPadding = context.getResources().getDimension(
            R.dimen.spacing_normal);
    }

    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Spinner child, View dependency) {
        return dependency instanceof Toolbar;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Spinner child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (mStartToolbarPosition - getStatusBarHeight());
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
            * (1f - expandedPercentageFactor)) + (child.getHeight()/2);

        float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
            * (1f - expandedPercentageFactor)) + (child.getWidth()/2);

        float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * (1f - expandedPercentageFactor));

        child.setY(mStartYPosition - distanceYToSubtract);
        child.setX(mStartXPosition - distanceXToSubtract);

        int proportionalAvatarSize = (int) (mAvatarMaxSize * (expandedPercentageFactor));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (mStartHeight - heightToSubtract);
        lp.height = (int) (mStartHeight - heightToSubtract);
        child.setLayoutParams(lp);
        return true;
    }

    private void maybeInitProperties(Spinner child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY());

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight() /2);

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (mStartXPosition == 0)
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));


        //controls the log start position, when the bar is collapsed
        if (mFinalXPosition == 0)
            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + ((int) mCustomFinalHeight  );

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY() + (dependency.getHeight()/2);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
