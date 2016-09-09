package com.hjs.mymztu.ui;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.hjs.mymztu.R;
import com.hjs.mymztu.constants.glide.ProgressTarget;
import com.hjs.mymztu.utils.XLog;

import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
    }

    private View initContentView(){
        RecyclerView list = new RecyclerView(TestActivity.this);
        list.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT));
        list.setLayoutManager(new LinearLayoutManager(TestActivity.this));
        list.setAdapter(new ProgressAdapter(Arrays.asList(
                // few results from https://www.google.com/search?tbm=isch&q=image&tbs=isz:lt,islt:4mp
                "http://www.noaanews.noaa.gov/stories/images/goes-12%2Dfirstimage-large081701%2Ejpg",
                "http://d.hiphotos.baidu.com/zhidao/pic/item/5882b2b7d0a20cf45a94cd9376094b36acaf9971.jpg",
                "http://img.sj33.cn/uploads/allimg/201105/20110517103510573.jpg",
                "http://pic41.nipic.com/20140521/5241227_111910725137_2.jpg",
                "http://pic.58pic.com/58pic/13/17/94/52M58PIC3VR_1024.jpg",
                "http://sc.jb51.net/uploads/allimg/141231/10-141231213KRH.jpg",
                "http://pic21.nipic.com/20120602/5252423_182253344000_2.jpg",
                "http://www.shifenkafei.com/data/upload/553deb1621af2.jpg",
                "http://www.zastavki.com/pictures/originals/2013/Photoshop_Image_of_the_horse_053857_.jpg",
                "http://hiphotos.baidu.com/90008com/pic/item/fdaef1ea2eea7ff3d539c904.jpeg",
                "http://www.pp3.cn/uploads/allimg/111124/1503233150-0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=3536931170,1097599943&fm=206&gp=0.jpg",
                "http://pic5.nipic.com/20100116/1295091_133628809603_2.jpg",
                "http://www.cesbio.ups-tlse.fr/multitemp/wp-content/uploads/2015/07/Mad%C3%A8re-022_0_1.jpg",
                "https://www.spacetelescope.org/static/archives/fitsimages/large/slawomir_lipinski_04.jpg",
                "http://img.sucai.redocn.com/attachments/images/201002/20100225/Redocn_2010012722444926.jpg",
                "http://photo.enterdesk.com/2010-11-29/enterdesk.com-21C9FD447B74B76B634E32ACDA1EEC84.jpg"
        )));
        return list;
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView text;
        private final ProgressBar progress;
        /** Cache target because all the views are tied to this view holder. */
        private final ProgressTarget<String, Bitmap> target;
        ProgressViewHolder(View root) {
            super(root);
            image = (ImageView)root.findViewById(R.id.image);
            text = (TextView)root.findViewById(R.id.text);
            progress = (ProgressBar)root.findViewById(R.id.progress);
            target = new MyProgressTarget<>(new BitmapImageViewTarget(image), progress, image, text);
        }
        void bind(String url) {
            target.setModel(url); // update target's cache
            Glide.with(image.getContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.github_232_progress)
                    .centerCrop() // needs explicit transformation, because we're using a custom target
                    .into(target);
        }
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
    private static class MyProgressTarget<Z> extends ProgressTarget<String, Z> {
        private final TextView text;
        private final ProgressBar progress;
        private final ImageView image;
        public MyProgressTarget(Target<Z> target, ProgressBar progress, ImageView image, TextView text) {
            super(target);
            this.progress = progress;
            this.image = image;
            this.text = text;
        }

        @Override
        public float getGranualityPercentage() {
            return 0.1f; // this matches the format string for #text below
        }

        @Override
        protected void onConnecting() {
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);
            image.setImageLevel(0);
            text.setVisibility(View.VISIBLE);
            text.setText("connecting");
        }
        @Override
        protected void onDownloading(long bytesRead, long expectedLength) {
            progress.setIndeterminate(false);
            progress.setProgress((int)(100 * bytesRead / expectedLength));
            image.setImageLevel((int)(10000 * bytesRead / expectedLength));
            text.setText(String.format("downloading %.2f/%.2f MB %.1f%%",
                    bytesRead / 1e6, expectedLength / 1e6, 100f * bytesRead / expectedLength));
        }
        @Override
        protected void onDownloaded() {
            progress.setIndeterminate(true);
            image.setImageLevel(10000);
            text.setText("decoding and transforming");
            XLog.e("TestActivity-->MyProgressTarget-->onDownloaded()","onDownloaded");
        }
        @Override
        protected void onDelivered() {
            XLog.e("TestActivity-->MyProgressTarget-->onDelivered()","onDelivered");
            progress.setVisibility(View.INVISIBLE);
            image.setImageLevel(0); // reset ImageView default
            text.setVisibility(View.INVISIBLE);
        }
    }

    private static class ProgressAdapter extends RecyclerView.Adapter<ProgressViewHolder> {
        private final List<String> models;
        public ProgressAdapter(List<String> models) {
            this.models = models;
        }
        @Override
        public ProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.github_232_item, parent, false);
            return new ProgressViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ProgressViewHolder holder, int position) {
            holder.bind(models.get(position));
        }
        @Override
        public int getItemCount() {
            return models.size();
        }
    }
}
