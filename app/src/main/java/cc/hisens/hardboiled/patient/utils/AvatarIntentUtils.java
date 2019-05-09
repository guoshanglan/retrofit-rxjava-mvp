package cc.hisens.hardboiled.patient.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

import androidx.core.content.FileProvider;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.utils
 * @fileName AvatarIntentUtils
 * @date on 2017/6/14 10:00
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 *
 * 拍照和相册选择图片
 */

public class AvatarIntentUtils {

    private static File tempFile;

    public static Intent getCameraCaptureIntent(Uri dest) {
        // 启动手机相机拍摄照片作为头像
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, dest);
        return intent;
    }

    /**
     * 打开相机获取图片
     */
    private  Intent getPicFromCamera(Context context) {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "cc.hisens.hardboiled.doctor", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }


        return  intent;
    }


    /**
     * 打开相机获取图片
     */

    public static Intent getGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("return-data", true);
        return intent;
    }


    /**
     * 裁剪图片
     */
    public static Intent cropRawPhotoIntent(Uri source, Uri dest) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(source, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("output", dest);
        //        intent.putExtra("scale", true);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // true:不返回uri，false：返回uri
        intent.putExtra("return-data", false);
        return intent;
    }


}
