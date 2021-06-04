package com.basis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.basis.ui.BaseActivity;
import com.basis.widget.interfaces.IWrapBar;
import com.bcq.mvvm.IModel;
import com.bcq.mvvm.IView;
import com.bcq.mvvm.IViewModel;
import com.bcq.mvvm.ViewHolder;
import com.bcq.mvvm.ViewModel;
import com.kit.UIKit;
import com.kit.utils.Logger;
import com.kit.utils.ScreenUtil;
import com.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PreviewActivity extends BaseActivity {
    private final PreModel preModel = new PreModel();

    public static void preview(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putExtra("uri", uri);
        activity.startActivity(intent);
    }

    public static void preview(Activity activity, String url) {
        Intent in = new Intent();
        in.setAction(activity.getApplicationInfo().packageName + ".preview");
        in.setData(Uri.parse(url));
        activity.startActivity(in);
    }


    @Override
    public IViewModel setViewModel() {
        IViewModel vm = new PreViewModel();
        vm.setModel(preModel);
        return vm;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        String dataString = intent.getDataString();
        if (!TextUtils.isEmpty(dataString)) {
            preModel.uri = Uri.parse(dataString);
        } else {
            preModel.uri = (Uri) intent.getParcelableExtra("uri");
        }
        if (null == preModel.uri) {
            Toast.makeText(this, "No Preview Uri Set !", Toast.LENGTH_LONG).show();
            return;
        }
        Logger.e(TAG, "previewUri = " + preModel.uri);
        initWrapBar();
    }


    private void initWrapBar() {
        getWrapBar()
                .setTitle(R.string.basis_preview)
                .addOptionMenu("旋转")
                .setBackHide(false)
                .setOnMenuSelectedListener(new IWrapBar.OnMenuSelectedListener() {
                    @Override
                    public void onItemSelected(int position) {
                        if (0 == position) {
                            preModel.rotation += 90;
                            Logger.e("rotation = " + preModel.rotation);
                            if (null != getViewModel()) getViewModel().refreshByCmd(0,preModel.rotation);
                        }
                    }
                })
                .work();
    }

    private static class PreModel implements IModel {
        public Uri uri;
        public int rotation;

        @Override
        public void release() {
        }
    }

    private static class PreViewModel extends ViewModel<PreModel> {
        PreViewModel() {
            super(UIKit.inflate(R.layout.basis_preview));
        }

        @Override
        public void onConvert(PreModel model, int action, Object extra) {
            PhotoView photoView = (PhotoView) iView.getView(R.id.iv_photo);
            if (ACTION_BIND == action) {
                photoView.setRotationTo(model.rotation);
                Picasso.get()
                        .load(model.uri)
                        .transform(new CropSquareTransformation())
                        .error(R.drawable.svg_img_default)
                        .placeholder(R.drawable.svg_image_loading)
                        .into(photoView);
            } else if (0 == action) {
                photoView.setRotationTo(model.rotation);
            }
        }
    }

    /**
     * 等比压缩值全屏显示
     * 压缩图片至和屏幕等宽或等高
     */
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int w = source.getWidth();
            int h = source.getHeight();
            Logger.e("PreviewActivity", "原图：w = " + w + " h= " + h);
            float sc_x = 1.0f * ScreenUtil.getScreemWidth() / w;
            float sc_y = 1.0f * ScreenUtil.getScreemHeight() / h;
            float scale = Math.min(sc_x, sc_y);//较小者
            Logger.e("PreviewActivity", "scale = " + scale + " scaleWidth= " + sc_x + " scaleHeight = " + sc_y);
            if (scale > 1) {
                Logger.e("使用原图");
                return source;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap result = Bitmap.createBitmap(source, 0, 0, w, h, matrix, true);
            source.recycle();
            Logger.e("PreviewActivity", "缩略：w = " + result.getWidth() + " h= " + result.getHeight());
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
}
