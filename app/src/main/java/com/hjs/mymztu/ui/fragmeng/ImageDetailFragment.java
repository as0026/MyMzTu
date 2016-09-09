package com.hjs.mymztu.ui.fragmeng;

 /*                   	 _ooOoo_
     *                  o8888888o
     *                  88" . "88
     *                  (| -_- |)
     *                  O\  =  /O
     *               ____/`—'\____
     *             .'  \\|     |//  `.
     *            /  \\|||  :  |||//  \
     *           /  _||||| -:- |||||-  \
     *           |   | \\\  -  /// |   |
     *           | \_|  ''\—/''  |_/ |
     *           \  .-\__  '-'  ___/-. /
     *          __'. .'  /—.—\  `. .' __
     *      ."" '<  `.___\_<|>_/___.'  >'"".
     *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     *     \  \ `-.   \_ __\ /__ _/   .-` /  /
     *======`-.____`-.___\_____/___.-`____.-'======
     *                   `=—='
     * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     *                佛祖保佑       永无BUG
     * 佛曰:
	 *               写字楼里写字间，写字间里程序员；
	 *               程序人员写程序，又拿程序换酒钱。
	 *               酒醒只在网上坐，酒醉还来网下眠；
	 *               酒醉酒醒日复日，网上网下年复年。
	 *               但愿老死电脑间，不愿鞠躬老板前；
	 *               奔驰宝马贵者趣，公交自行程序员。
	 *               别人笑我忒疯癫，我笑自己命太贱；
	 *               不见满街漂亮妹，哪个归得程序员？
     */

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.hjs.mymztu.R;
import com.hjs.mymztu.constants.glide.ProgressTarget;
import com.hjs.mymztu.utils.XLog;
import com.hjs.mymztu.widget.photoview.PhotoViewAttacher;
import com.hjs.mymztu.widget.photoview.PhotoViewAttacher.OnPhotoTapListener;

import app.dinus.com.loadingdrawable.LoadingView;

public class ImageDetailFragment extends Fragment {

	private static final String TAG = "ImageDetailFragment";

	private String mImageUrl;
	private LoadingView mImageView;
	private PhotoViewAttacher mAttacher = null;
	private ProgressTarget<String, Bitmap> target;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (LoadingView) v.findViewById(R.id.image);
//		WaterBottleLoadingRenderer.Builder builder = new WaterBottleLoadingRenderer.Builder(mImageView.getContext());
//		mImageView.setLoadingRenderer(builder.build());
//		mImageView.setImageDrawable(new LoadingDrawable(builder.build()));
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		XLog.e("ImageDetailFragment-->onActivityCreated()","mImageUrl="+mImageUrl);
		target = new MyProgressTarget<>(new BitmapImageViewTarget(mImageView),mImageView);
		target.setModel(mImageUrl);

		SimpleTarget<Bitmap> mTarget = new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
				if(resource != null){
					mImageView.setImageBitmap(resource);
					mAttacher.update();
				}
			}
			@Override
			public void onLoadFailed(Exception e, Drawable errorDrawable) {
				super.onLoadFailed(e, errorDrawable);
			}
		};
		Glide.with(getActivity().getApplicationContext())
				.load(mImageUrl)
				.asBitmap()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.error(R.mipmap.card_photot)
//				.placeholder(R.drawable.github_232_progress)
				.centerCrop() // needs explicit transformation, because we're using a custom target
				.into(mTarget);
	}

	/**
	 * Demonstrates 3 different ways of showing the progress:
	 * <ul>
	 * <li>Update a full fledged progress bar</li>
	 * <li>Update a text view to display size/percentage</li>
	 * <li>Update the placeholder via Drawable.level</li>
	 * </ul>
	 * This last one is tricky: the placeholder that Glide sets can be used as a progress drawable
	 * without any extra Views in the view hierarchy if it supports levels via <code>usesLevel="true"</code>
	 * or <code>level-list</code>.
	 *
	 * @param <Z> automatically match any real Glide target so it can be used flexibly without reimplementing.
	 */
	private class MyProgressTarget<Z> extends ProgressTarget<String, Z> {
		private LoadingView image;
		public MyProgressTarget(Target<Z> target, LoadingView image) {
			super(target);
			this.image = image;
		}

		@Override
		public float getGranualityPercentage() {
			return 0.1f; // this matches the format string for #text below
		}

		@Override
		protected void onConnecting() {
			XLog.e("ImageDetailFragment-->MyProgressTarget-->onConnecting()","onConnecting");
//			image.setImageLevel(0);
//			LoadingDrawable mGearDrawable = new LoadingDrawable(new WaterBottleLoadingRenderer(image.getContext()));
//			image.setImageDrawable(mGearDrawable);
//			mGearDrawable.start();
		}
		@Override
		protected void onDownloading(long bytesRead, long expectedLength) {
//			XLog.e("ImageDetailFragment-->MyProgressTarget-->onDownloading()",bytesRead+"-"+expectedLength);
//			image.setImageLevel((int)(10000 * bytesRead / expectedLength));
		}
		@Override
		protected void onDownloaded() {
//			image.setImageLevel(10000);
			XLog.e("ImageDetailFragment-->MyProgressTarget-->onDownloaded()","onDownloaded");
			mAttacher.update();
		}
		@Override
		protected void onDelivered() {
			XLog.e("ImageDetailFragment-->MyProgressTarget-->onDelivered()","onDelivered");
			mAttacher.update();
		}
	}

}
