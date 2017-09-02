package com.blanke.mdwechat.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.blanke.mdwechat.Common;
import com.blanke.mdwechat.config.HookConfig;
import com.blanke.mdwechat.util.DrawableUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by blanke on 2017/8/1.
 */

public class ConversationHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        final Bitmap background = DrawableUtils.getExternalStorageAppBitmap(Common.CONVERSATION_BACKGROUND_FILENAME);
        if (background != null) {
            xMethod(wxConfig.classes.ConversationFragment,
                    wxConfig.methods.MainFragment_onTabCreate,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            View listView = (View) getObjectField(param.thisObject, wxConfig.fields.ConversationFragment_mListView);
                            listView.setBackground(new BitmapDrawable(background));
                        }
                    });
        }
        if (HookConfig.isHookripple()) {
            xMethod(wxConfig.classes.ConversationAdapter,
                    "getView", int.class, View.class, ViewGroup.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            View view = (View) param.getResult();
                            view.setBackground(getRippleDrawable(view.getContext()));
                        }
                    });
        }
    }
}
