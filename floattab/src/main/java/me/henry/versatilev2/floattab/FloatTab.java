package me.henry.versatilev2.floattab;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by henry on 2018/1/16.
 */

public class FloatTab extends LinearLayout {
    private long ANIMATION_DURATION = 500L;
    private long START_DELAY = 150L;
    private float MAIN_ROTATION_START = 0f;
    private float MAIN_ROTATION_END = 405f;
    private float ITEM_ROTATION_START = 180f;
    private float ITEM_ROTATION_END = 360f;
    private float ROLL_UP_ROTATION_START = -45f;
    private float ROLL_UP_ROTATION_END = 360f;
    OnFoldingItemSelectedListener onFoldingItemClickListener = null;
    OnMainButtonClickedListener onMainButtonClickListener = null;

    private List<SelectedMenuItem> mData;

    private AnimatorSet mExpandingSet = new AnimatorSet();
    private AnimatorSet mRollupSet = new AnimatorSet();
    private boolean isAnimating = false;

    private MenuBuilder mMenu;

    private int mSize = 0;
    private int indexCounter = 0;
    private ImageView mainImageView = new ImageView(getContext());
    private ImageView selectedImageView = null;
    private int selectedIndex = 0;

    private int itemsPadding = 0;
    private int drawableResource = 0;
    private int selectionColor = 0;
    private Context mContext;


    public FloatTab(Context context) {
        this(context, null);
    }

    public FloatTab(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mMenu = new MenuBuilder(context);
        setGravity(Gravity.RIGHT);

        if (getBackground() == null) {
            setBackgroundResource(R.drawable.background_tabbar);
        }
        TypedArray a = initAttrs(attrs, defStyleAttr);

        mSize = getSizeDimension();
        initViewTreeObserver(a);
    }
    private int getSizeDimension() {
        return getResources().getDimensionPixelSize(R.dimen.ftb_size_normal);
    }

    /**
     * Initializing attributes
     */
    private TypedArray initAttrs(AttributeSet attrs, int defStyleRes) {
        return mContext.obtainStyledAttributes(attrs,
                R.styleable.FoldingTabBar, 0,
                defStyleRes);
    }

    /**
     * When folding tab bar pre-draws we should initialize
     * inflate our menu, and also add menu items, into the
     * FoldingTabBar, also here we are initializing animators
     * and animation sets
     */
    private void initViewTreeObserver(final TypedArray a) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                isAnimating = true;
                initAttributesValues(a);
                initExpandAnimators();
                initRollUpAnimators();
                select(selectedIndex);
                return true;
            }
        });

    }

    private void select(int position) {
        selectedImageView = (SelectedMenuItem) getChildAt(position);
        selectedImageView.setActivated(true);

    }

    /**
     * Here we are initializing default values
     * Also here we are binding new attributes into this values
     *
     * @param a - incoming typed array with attributes values
     */
    private void initAttributesValues(TypedArray a) {
        drawableResource = R.drawable.ic_action_plus;
        itemsPadding = getItemsPadding();
        selectionColor = R.color.ftb_selected_dot_color;
        if (a.hasValue(R.styleable.FoldingTabBar_mainImage)) {
            drawableResource = a.getResourceId(R.styleable.FoldingTabBar_mainImage, 0);
        }
        if (a.hasValue(R.styleable.FoldingTabBar_itemPadding)) {
            itemsPadding = a.getDimensionPixelSize(R.styleable.FoldingTabBar_itemPadding, 0);
        }
        if (a.hasValue(R.styleable.FoldingTabBar_selectionColor)) {
            selectionColor = a.getResourceId(R.styleable.FoldingTabBar_selectionColor, 0);
        }
        if (a.hasValue(R.styleable.FoldingTabBar_menu)) {
            inflateMenu(a.getResourceId(R.styleable.FoldingTabBar_menu, 0));
        }
    }

    /**
     * This is the padding for menu items
     */
    private int getItemsPadding() {
        return getResources().getDimensionPixelSize(R.dimen.ftb_item_padding);
    }
    private int getMainButtonPadding() {
        return getResources().getDimensionPixelSize(R.dimen.ftb_main_button_padding);
    }
    /**
     * Getting SupportMenuInflater to get all visible items from
     * menu object
     */
    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(mContext);
    }

    /**
     * @param menuItem object from Android Sdk. This is same menu item
     *                 that you are using e.g in NavigationView or any kind of native menus
     */
    private SelectedMenuItem initAndAddMenuItem(final MenuItemImpl menuItem) {
        final SelectedMenuItem item = new SelectedMenuItem(mContext, selectionColor);
        item.setImageDrawable(menuItem.getIcon());
        item.setLayoutParams(new ViewGroup.LayoutParams(mSize, mSize));
        item.setPadding(itemsPadding, itemsPadding, itemsPadding, itemsPadding);
        item.setVisibility(GONE);
        item.setActivated(menuItem.isChecked());
        addView(item, indexCounter);
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFoldingItemClickListener != null) {
                    onFoldingItemClickListener.onFoldingItemSelected(menuItem);
                }
                menuItem.setChecked(true);
                if (selectedImageView != null) {
                    selectedImageView.setActivated(false);
                }
                selectedImageView = item;
                selectedIndex = indexOfChild(item);
                animateMenu();
            }
        });
        indexCounter++;


        return item;
    }
    /**
     * Main button (+/x) initialization
     * Adding listener to the main button click
     */
    private void initMainButton(int mainButtonIndex) {
        int mainButtonPadding = getMainButtonPadding();
        mainImageView.setImageResource(drawableResource);
        mainImageView.setLayoutParams(new ViewGroup.LayoutParams(mSize, mSize));
        mainImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnMainButtonClickedListener listener = getOnMainButtonClickListener();
                if (listener != null) {
                    listener.onMainButtonClicked();
                }
                animateMenu();
            }
        });

        addView(mainImageView, 4);
        mainImageView.setPadding(mainButtonPadding, mainButtonPadding, mainButtonPadding, mainButtonPadding);
    }
    /**
     * measuredWidth - mSize = 0 we can understand that our menu is closed
     * But on some devices I've found a case when we don't have exactly 0. So
     * now we defined some range to understand what is the state of our menu
     */
    private void animateMenu() {
        int range = (getMeasuredWidth() - mSize);
        if (range >= -2 && range <= 2) {
            expand();
        } else {
            rollUp();
        }
    }

    /**
     * Menu inflating, we are getting list of visible items,
     * and use them in method @link initAndAddMenuItem
     * Be careful, don't use non-odd number of menu items
     * FTB works not good for such menus. Anyway you will have an exception
     *
     * @param resId your menu resource id
     */
    private void inflateMenu(int resId) {
        getMenuInflater().inflate(resId, mMenu);
        if (mMenu.getVisibleItems().size() % 2 != 0) {
            try {
                throw new OddMenuItemsException();
            } catch (OddMenuItemsException e) {
                e.printStackTrace();
            }
        }

        ArrayList<MenuItemImpl> implItems = mMenu.getVisibleItems();
        List<SelectedMenuItem> datas = new ArrayList<>();
        for (MenuItemImpl item : implItems) {
            datas.add(initAndAddMenuItem(item));
        }
        mData = datas;

        initMainButton(mMenu.getVisibleItems().size() / 2);
    }



    /**
     * Expand animation. Whole animators
     */

    private void initExpandAnimators() {
        mExpandingSet.setDuration(ANIMATION_DURATION);

        int destWidth = getChildCount() * mSize;
        AnimatorSet rotationSet = new AnimatorSet();
        AnimatorSet scalingSet = new AnimatorSet();

        ValueAnimator scalingAnimator = ValueAnimator.ofInt(mSize, destWidth);
        scalingAnimator.addUpdateListener(scaleAnimator);
        scalingAnimator.addListener(rollUpListener);

        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(MAIN_ROTATION_START, MAIN_ROTATION_END);

        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mainImageView.setRotation(value);
            }
        });
        for (final SelectedMenuItem item : mData) {
            final ValueAnimator it = ValueAnimator.ofFloat(ITEM_ROTATION_START, ITEM_ROTATION_END);
            it.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = it.getAnimatedFraction();
                    item.setScaleX(fraction);
                    item.setScaleY(fraction);
                    item.setRotation((float) it.getAnimatedValue());
                }
            });
            it.addListener(expandingListener);
            rotationSet.playTogether(it);
        }

        scalingSet.playTogether(scalingAnimator, rotationAnimator);
        scalingSet.setInterpolator(new CustomBounceInterpolator());
        rotationSet.setInterpolator(new BounceInterpolator());
        rotationSet.setStartDelay(START_DELAY);
        mExpandingSet.playTogether(scalingSet, rotationSet);
    }

    /**
     * Roll-up animators. Whole roll-up animation
     */
    private void initRollUpAnimators() {
        mRollupSet.setDuration(ANIMATION_DURATION);

        int destWidth = mMenu.size() * mSize;

        AnimatorSet rotationSet = new AnimatorSet();

        ValueAnimator scalingAnimator = ValueAnimator.ofInt(destWidth, mSize);
        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(ROLL_UP_ROTATION_START, ROLL_UP_ROTATION_END);

        scalingAnimator.addUpdateListener(scaleAnimator);
        mRollupSet.addListener(rollUpListener);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mainImageView.setRotation(value);
            }
        });

        AnimatorSet scalingSet = new AnimatorSet();
        scalingSet.playTogether(scalingAnimator, rotationAnimator);
        scalingSet.setInterpolator(new CustomBounceInterpolator());
        rotationSet.setInterpolator(new BounceInterpolator());
        mRollupSet.playTogether(scalingSet, rotationSet);
    }

    /**
     * These two public functions can be used to open our menu
     * externally
     */
    private void expand() {
        mExpandingSet.start();
    }

    private void rollUp() {
        mRollupSet.start();
    }

    /**
     * Here we are resolving sizes of your Folding tab bar.
     * Depending on
     *
     * @param measureSpec we can understand what kind of parameters
     *                    do you using in your layout file
     *                    In case if you are using wrap_content, we are using @dimen/ftb_size_normal
     *                    by default
     *                    <p>
     *                    In case if you need some custom sizes, please use them)
     */

    private int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case View.MeasureSpec.UNSPECIFIED:
                return desiredSize;
            case View.MeasureSpec.AT_MOST:
                return Math.min(desiredSize, specSize);
            case View.MeasureSpec.EXACTLY:
                return specSize;
            default:
                return desiredSize;
        }

    }

    /**
     * Here we are overriding onMeasure and here we are making our control
     * squared
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isAnimating) {
            int preferredSize = getSizeDimension();
            mSize = resolveAdjustedSize(preferredSize, widthMeasureSpec);
            setMeasuredDimension(mSize, mSize);
        }
    }

    /**
     * Here we are saving view state
     */
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.selection = selectedIndex;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        //  super.onRestoreInstanceState(state);
        SavedState st = (SavedState) state;
        super.onRestoreInstanceState(st.getSuperState());
        selectedIndex = st.selection;
    }

    /**
     * Here we are restoring view state (state, selection)
     */


    ValueAnimator.AnimatorUpdateListener scaleAnimator = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //真系叼你老母
            ViewGroup.LayoutParams layoutParams =getLayoutParams();
            layoutParams.width = (int) animation.getAnimatedValue();
            setLayoutParams(layoutParams);

        }
    };

    /**
     * Here we should hide all items, and deactivate menu item
     */
    private Animator.AnimatorListener rollUpListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            for (SelectedMenuItem item : mData) {
                item.setVisibility(View.GONE);
            }
            if (selectedImageView != null) {
                selectedImageView.setActivated(false);
            }

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    /**
     * This listener we need to show our Menu items
     * And also after animation was finished we should activate
     * our SelectableImageView
     */
    private Animator.AnimatorListener expandingListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            for (SelectedMenuItem item : mData) {
                item.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (selectedImageView != null) {
                selectedImageView.setActivated(true);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public void setOnFoldingItemClickListener(OnFoldingItemSelectedListener onFoldingItemClickListener) {
        this.onFoldingItemClickListener = onFoldingItemClickListener;
    }

    public OnMainButtonClickedListener getOnMainButtonClickListener() {
        return onMainButtonClickListener;
    }

    public void setOnMainButtonClickListener(OnMainButtonClickedListener onMainButtonClickListener) {
        this.onMainButtonClickListener = onMainButtonClickListener;
    }

    /**
     * Listener for handling events on folding items.
     */
    public interface OnFoldingItemSelectedListener {
        /**
         * Called when an item in the folding tab bar menu is selected.
         *
         * @param item The selected item
         *             *
         *             *
         * @return true to display the item as the selected item
         */
        boolean onFoldingItemSelected(MenuItem item);
    }

    /**
     * Listener for handling events on folding main button
     */
    public interface OnMainButtonClickedListener {
        /**
         * Called when the main button was pressed
         */
        void onMainButtonClicked();
    }

    /**
     * We have to save state and selection of our View
     */
    public static final class SavedState extends BaseSavedState {
        int selection = 0;

        public SavedState(@NotNull Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel inp) {
            super(inp);
            this.selection = inp.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(selection);
        }

        @NotNull
        public static final Creator CREATOR = (Creator) (new Creator() {
            @NotNull
            public FloatTab.SavedState createFromParcel(@NotNull Parcel source) {
                return new FloatTab.SavedState(source);
            }

//            @NotNull
//            public FoldingTab.SavedState[] newArray(int size) {
//                return new FoldingTab.SavedState[size];
//            }


            public Object[] newArray(int var1) {
                return (Object[]) this.newArray(var1);
            }
        });


    }


}
