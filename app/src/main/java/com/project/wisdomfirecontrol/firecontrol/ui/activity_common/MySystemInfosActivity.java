package com.project.wisdomfirecontrol.firecontrol.ui.activity_common;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.mvp_0726.common.utils.AppManager;
import com.mvp_0726.project_0726.login.ui.ChangePasswordActivity;
import com.project.wisdomfirecontrol.R;
import com.project.wisdomfirecontrol.common.base.BaseActivity;
import com.project.wisdomfirecontrol.common.base.Global;
import com.project.wisdomfirecontrol.common.base.UserInfo;
import com.project.wisdomfirecontrol.common.base.UserManage;
import com.project.wisdomfirecontrol.common.util.SharedPreUtil;
import com.project.wisdomfirecontrol.firecontrol.ui.activity_login_splash.SelectUnitOrSepuviseActivity;
import com.project.wisdomfirecontrol.firecontrol.ui.utils.PhotoUtils;
import com.project.wisdomfirecontrol.firecontrol.ui.utils.Unit_StringUtils;
import com.project.wisdomfirecontrol.firecontrol.ui.view.dialog.SuccessDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 * 我的
 */

public class MySystemInfosActivity extends BaseActivity {
    private LinearLayout selechPhoto, tv_change_pwd, tv_app_dail, tv_user_back;
    private TextView tv_user_name, tv_user_photo_num, tv_title;
    private ImageView iv_user_logo;
    private View view;
    private UserInfo userInfos;
    private String userName;
    private String password;
    private String telNum;
    private List<LocalMedia> selectList = new ArrayList<>();
    private PhotoUtils photoUtils;
    private StringBuffer stringBuffer;
    private String imageurl;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView() {
        tv_title = findView(R.id.tv_title);
        tv_title.setText("系统设置");
        tv_user_name = findView(R.id.tv_user_name);
        tv_user_photo_num = findView(R.id.tv_user_photo_num);
        selechPhoto = findView(R.id.tv_user_name_photo);
        iv_user_logo = findView(R.id.iv_user_logo);
        tv_change_pwd = findView(R.id.tv_change_pwd);
        tv_app_dail = findView(R.id.tv_app_dail);
        tv_user_back = findView(R.id.tv_user_back);
        view = findView(R.id.view);
        iv_user_logo.setImageResource(R.drawable.icon_app);
        if (photoUtils == null) {
            photoUtils = new PhotoUtils(MySystemInfosActivity.this, selectList, iv_user_logo, view, 1);
        }
    }

    @Override
    public void initListener() {
        selechPhoto.setOnClickListener(this);
        tv_change_pwd.setOnClickListener(this);
        tv_app_dail.setOnClickListener(this);
        tv_user_back.setOnClickListener(this);
        iv_user_logo.setOnClickListener(this);
    }

    @Override
    public void initData() {
        userInfos = Unit_StringUtils.getUserInfos(Global.mContext);
        if (userInfos != null) {
            userName = userInfos.getUserName();
            telNum = UserManage.getInstance().getUserPhoneInfo(Global.mContext).getTelNum();
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(telNum)) {
                tv_user_name.setText(userName);
                tv_user_photo_num.setText(telNum);
            }
        }
    }

    @Override
    public void initTitleHeight(LinearLayout linearLayout) {

    }

    @Override
    public void onClick(View v, int id) {
        switch (v.getId()) {
            case R.id.tv_user_name_photo:
                selectUserPhotos();
                break;
            case R.id.iv_user_logo:
                selectUserPhotos();
                break;
            case R.id.tv_change_pwd:

                changePwd();
                break;
            case R.id.tv_app_dail:
                tv_app_dail();
                break;
            case R.id.tv_user_back:
                tv_user_back();
                break;
        }
    }

    public void selectUserPhotos() {
        if (photoUtils != null) {
            photoUtils.showSelectVideoOrPhoto("photo", "activity");
        }
    }

    private void changePwd() {
        Intent intent = new Intent(MySystemInfosActivity.this, ChangePasswordActivity.class);
        super.startActivity(intent);
    }

    private void tv_app_dail() {
        Intent intent = new Intent(MySystemInfosActivity.this, AboutVersionCodeActivity.class);
        super.startActivity(intent);
    }

    private boolean isRestart = true;

    private void tv_user_back() {
        final SuccessDialog successDialog = new SuccessDialog(MySystemInfosActivity.this);
        successDialog.setContent(getString(R.string.iscome_back_login));
        if (!isFinishing()) {
            successDialog.show();
        }
        successDialog.setDialogCallback(new SuccessDialog.Dialogcallback() {
            @Override
            public void dialogdo(LinearLayout container) {
                SharedPreUtil.saveBoolean(Global.mContext, "isLogin", false);
                SharedPreUtil.saveString(Global.mContext, "userinfos", "");

                if (isRestart) {
                    Intent intent = new Intent(MySystemInfosActivity.this, SelectUnitOrSepuviseActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    AppManager.getAppManager().removedAlllActivity(MySystemInfosActivity.this);
                    isRestart = false;
                    finish();
                }
            }

            @Override
            public void dialogcancle() {
                if (!isFinishing()) {
                    successDialog.dismiss();
                }
            }
        });
        successDialog.isCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "onActivityResult:resultCode " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    selectList = PictureSelector.obtainMultipleResult(data);
                    LocalMedia media = selectList.get(0);
                    String compressPath = media.getCompressPath();
                    Log.d("tag", "onActivityResult: " + compressPath);
                    Glide.with(this).load(compressPath).into(iv_user_logo);
                    if (selectList.size() > 0) {
                        showWaitDialog(MySystemInfosActivity.this, "图片处理中...");
                        imageurl = reducePhoto(selectList);
                    }
                    break;
            }
        }
    }

    public String reducePhoto(final List<LocalMedia> dataList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer();
                }
                stringBuffer.setLength(0);
                for (int i = 0; i < dataList.size(); i++) {
                    if (i == (dataList.size() - 1)) {
                        stringBuffer.append(com.project.wisdomfirecontrol.common.util.StringUtils.getimage(dataList.get(i).getCompressPath())).append(",");
                    } else {
                        stringBuffer.append(com.project.wisdomfirecontrol.common.util.StringUtils.getimage(dataList.get(i).getCompressPath())).append(",");
                    }
                }
                imageurl = "";
                imageurl = stringBuffer.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissWaitDialog();
                    }
                });
            }
        }).start();

        return imageurl;
    }

    @Override
    public void onHttpSuccess(int reqType, Message obj) {
        super.onHttpSuccess(reqType, obj);
        dismissWaitDialog();
    }

    @Override
    public void onHttpError(int reqType, String error) {
        super.onHttpError(reqType, error);
        dismissWaitDialog();
    }
}
