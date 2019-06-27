package com.yunkang.chat.mine.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * 作者：凌涛 on 2019/1/21 16:18
 * 邮箱：771548229@qq..com
 */
public class DeleteRecyclerView extends RecyclerView {

    /**
     * 左右滑动时的速率大小，只要绝对值大于这个值，那么进行滑动还原
     */
    private final int mVelocityX = 1500;
    /**
     * 手势包装类，它帮助我们处理多点手势等复杂问题
     */
    private final GestureDetectorCompat mDetector;
    /**
     * 记录手势滑动item时的速率
     */
    private VelocityTrackerClass mVelocityTrackerClass;
    /**
     * 记录当前滑动的item对象
     */
    private RecyclerView.ViewHolder mTouchVieHolder;

    /**
     * item的点击事件
     */
    private OnItemClickListener mOnItemClickListener;

    private ReplyItemCallback mReplyItemCallback = new ReplyItemCallback() {
        public void callback(boolean isShowDeleteLayout) {
            if (!isShowDeleteLayout) {
                mTouchVieHolder = null;
            }
        }
    };

    public DeleteRecyclerView(Context context) {
        this(context, null);
    }

    public DeleteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DeleteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDetector = new GestureDetectorCompat(getContext().getApplicationContext(), new MyGestureCompat());
        mVelocityTrackerClass = new VelocityTrackerClass();
        // 将item的长按事件禁掉，如果不禁掉，则长按item不抬起手指进行拖动时，手势是接受不到事件的
        setLongClickable(false);
    }

    /**
     * 设置列表点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        super.setLongClickable(longClickable);
        mDetector.setIsLongpressEnabled(longClickable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mDetector.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mVelocityTrackerClass.clear();
            RecyclerView.ViewHolder tmp = getTouchViewHolder(e);
            if (mTouchVieHolder != null && !mTouchVieHolder.equals(tmp)) {
                if (mTouchVieHolder instanceof DeleteViewHolder) {
                    ((DeleteViewHolder) mTouchVieHolder).scrollReplyHolder(1, mReplyItemCallback);
                    return true;
                }
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) { // 手指抬起时
            if (mTouchVieHolder != null && mTouchVieHolder instanceof DeleteViewHolder) {  // 如果之前已经滑动过item了，那么将该item还原到最初位置或者完全显示删除按钮
                if (!mVelocityTrackerClass.isEmpty()) { // 有滑动速率，则对滑动速率进行判断，自动滑动，只判断横向速率
                    if (mVelocityTrackerClass.velocityX < -mVelocityX) { // 滑动到左边
                        ((DeleteViewHolder) mTouchVieHolder).scrollReplyHolder(2, mReplyItemCallback);
                    } else if (mVelocityTrackerClass.velocityX > mVelocityX) { // 滑动到右边
                        ((DeleteViewHolder) mTouchVieHolder).scrollReplyHolder(1, mReplyItemCallback);
                    } else {
                        ((DeleteViewHolder) mTouchVieHolder).scrollReplyHolder(3, mReplyItemCallback);
                    }
                } else {
                    ((DeleteViewHolder) mTouchVieHolder).scrollReplyHolder(3, mReplyItemCallback);
                }
                return true;
            } else { // 如果之前没有滑动过item，则调用父类方法，自由滑动整个列表
                return super.onTouchEvent(e);
            }
        }
        // 如果已经有记录的item了，则不再滑动列表了，单独处理该item
        if (mTouchVieHolder != null) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    // 根据手势获取当前点击的item对象
    private ViewHolder getTouchViewHolder(MotionEvent e) {
        View view = findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            return getChildViewHolder(view);
        }
        return null;
    }

    private interface ReplyItemCallback {
        /**
         * 当手指抬起时，还原item时的回调方法，内部监听，不对外开放
         *
         * @param isShowDeleteLayout 是否完全显示了item下面的删除控件
         */
        void callback(boolean isShowDeleteLayout);
    }

    /**
     * 用来记录当前滑动时的速率，只在拖动item时有效
     */
    private class VelocityTrackerClass {

        private float velocityX = 0.0f;

        private float velocityY = 0.0f;

        private void setVelocity(float x, float y) {
            velocityX = x;
            velocityY = y;
        }

        /**
         * 清空速率
         */
        private void clear() {
            velocityX = 0.0f;
            velocityY = 0.0f;
        }

        /**
         * 判断是否有速率
         */
        private boolean isEmpty() {
            return velocityX == 0.0f && velocityY == 0.0f;
        }

    }

    private class MyGestureCompat extends GestureDetector.SimpleOnGestureListener {

        /**
         * 获取手机设置的认为抖动像素点，每个手机可能得到的值不一样
         */
        private ViewConfiguration mViewConfiguration;

        public MyGestureCompat() {
            super();
            mViewConfiguration = ViewConfiguration.get(getContext().getApplicationContext());
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mTouchVieHolder != null && mTouchVieHolder instanceof DeleteViewHolder) {
                ((DeleteViewHolder) mTouchVieHolder).smoothScrollBy((int) distanceX);
                return true;
            } else {
                if (Math.abs(distanceX) > mViewConfiguration.getScaledTouchSlop() && Math.abs(distanceY) < mViewConfiguration.getScaledTouchSlop()) {
                    mTouchVieHolder = getTouchViewHolder(e2);
                    if (mTouchVieHolder != null && mTouchVieHolder instanceof DeleteViewHolder) {
                        ((DeleteViewHolder) mTouchVieHolder).smoothScrollBy((int) distanceX);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mVelocityTrackerClass.setVelocity(velocityX, velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = getTouchViewHolder(e);
                if (holder != null) {
                    mOnItemClickListener.onItemHolderClick(DeleteRecyclerView.this, holder, holder.getAdapterPosition());
                }
            }
            return super.onSingleTapConfirmed(e);
        }

    }

    public static class DeleteViewHolder extends RecyclerView.ViewHolder {

        private final int mDuration = 300;
        private final RelativeLayout mRootLayout;
        private final View mContentView;
        private View mDeleteLayout;

        private boolean isShowDeleteLayout = false;

        private int mMaxOffest = -1;
        /**
         * 阻尼系数
         */
        private float mDampingCoefficient = 1;

        private ObjectAnimator mReplyAnimator = null;

        public DeleteViewHolder(@NonNull View originalView) {
            super(originalView);
            mContentView = originalView;
            mContentView.setClickable(false);  //如果这里不设置item的外层布局为不可点击，则下面的删除按钮布局就接受不到点击事件
            mRootLayout = new RelativeLayout(mContentView.getContext().getApplicationContext());
            Field[] fields = RecyclerView.ViewHolder.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("itemView")) {
                    field.setAccessible(true);
                    try {
                        field.set(this, mRootLayout);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            initDefaultViews();
        }

        private void initDefaultViews() {
            TextView deleteLayout = new TextView(mContentView.getContext().getApplicationContext());
            deleteLayout.setBackgroundColor(Color.RED);
            deleteLayout.setGravity(Gravity.CENTER);
            deleteLayout.setText("Delete");
            deleteLayout.setTextColor(Color.WHITE);
            deleteLayout.setTextSize(20); // 设置字体大小
            int left_right_padd = 150;
            deleteLayout.setPadding(
                    left_right_padd,
                    deleteLayout.getPaddingTop(),
                    left_right_padd,
                    deleteLayout.getPaddingBottom()
            ); // 设置字体间距
            addDeleteLayout(deleteLayout);

            deleteLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里会和item的点击事件有冲突，解决办法是判断当前的deletelayout是否显示，如果显示了，则执行点击事件，没显示则不执行点击事件
                    if (isShowDeleteLayout) {
                        Toast.makeText(mRootLayout.getContext(), "Delete click position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        /**
         * 在每一个item布局的下面添加一个布局，此布局可自定义。默认情况下只包含一个删除按钮
         */
        public void addDeleteLayout(View view) {
            mDeleteLayout = view;
            mContentView.measure(0, 0);
            RelativeLayout.LayoutParams deleteLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mContentView.getMeasuredHeight());
            deleteLayoutParams.rightMargin = 5; // 右边设置一点边距，防止滑出的时候会出现一点点红色
            deleteLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            deleteLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mRootLayout.addView(mDeleteLayout, deleteLayoutParams);
            RelativeLayout.LayoutParams itemLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mRootLayout.addView(mContentView, itemLayoutParams);

            mDeleteLayout.measure(0, 0);
            mMaxOffest = mDeleteLayout.getMeasuredWidth();
        }

        private void smoothScrollBy(int dx) {
            float distance = mContentView.getTranslationX() - dx / mDampingCoefficient;
            if (distance < 0) {
                mContentView.setTranslationX(distance);
                float tX = Math.abs(mContentView.getTranslationX());
                if (tX > mMaxOffest) {
                    mDampingCoefficient = 1 + Math.abs(mContentView.getTranslationX() / mMaxOffest * 4);
                } else {
                    mDampingCoefficient = 1;
                }
            }
        }

        /**
         * 对滑动的item进行还原
         *
         * @param way      还原的方式：一共有三种方式
         *                 <li>1、强制还原到最右边，这个一般是第二次touch屏幕时，将上次的item强制还原，值传1
         *                 <li>2、强制还原到最左边，值传2
         *                 <li>3、手指离开屏幕时，无速率，则根据滑动的宽度是否大于删除布局宽度的一半来进行判断滑向左边还是右边，值传3<br>
         * @param callback 在还原动画结束之后的回调方法
         */
        private void scrollReplyHolder(final int way, final ReplyItemCallback callback) {
            if (mReplyAnimator != null && mReplyAnimator.isRunning()) {
                return;
            }
            if (way == 1) {
                mReplyAnimator = ObjectAnimator.ofFloat(mContentView, "translationX", mContentView.getTranslationX(), 0).setDuration(mDuration);
            } else if (way == 2) {
                mReplyAnimator = ObjectAnimator.ofFloat(mContentView, "translationX", mContentView.getTranslationX(), -mMaxOffest).setDuration(mDuration);
            } else if (way == 3) {
                if (Math.abs(mContentView.getTranslationX()) < mMaxOffest / 2) {
                    mReplyAnimator = ObjectAnimator.ofFloat(mContentView, "translationX", mContentView.getTranslationX(), 0).setDuration(mDuration);
                } else {
                    mReplyAnimator = ObjectAnimator.ofFloat(mContentView, "translationX", mContentView.getTranslationX(), -mMaxOffest).setDuration(mDuration);
                }
            } else {
                throw new RuntimeException("Error param ...");
            }
            mReplyAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (callback != null) {
                        if (mContentView.getTranslationX() == 0) {
                            mDampingCoefficient = 1;
                        }
                        isShowDeleteLayout = (mContentView.getTranslationX() != 0);
                        callback.callback(isShowDeleteLayout);
                    }
                }
            });
            mReplyAnimator.setInterpolator(new DecelerateInterpolator());
            mReplyAnimator.start();
        }

        /**
         * 外部获取item下面的删除控件的布局
         */
        public View getDeleteLayout() {
            return mDeleteLayout;
        }
    }

    public interface OnItemClickListener {

        /**
         * item点击事件
         *
         * @param currentListView   当前RecyclerView
         * @param currentHolderItem 当前点击的item对象
         * @param position          当前点击的位置
         */
        void onItemHolderClick(RecyclerView currentListView, RecyclerView.ViewHolder currentHolderItem, int position);

    }

}

