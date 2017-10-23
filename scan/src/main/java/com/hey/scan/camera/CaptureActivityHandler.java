package com.hey.scan.camera;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.hey.scan.R;


/**
 * Author: Vondear
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {
    DecodeThread decodeThread;
    private State state;
    private decodeListener mDecodeListener;
    private Context mContext;

    public CaptureActivityHandler(Context context) {
        decodeThread = new DecodeThread(this,context);
        decodeThread.start();
        state = State.SUCCESS;
        mContext=context;
        CameraManager.getInstance(mContext).startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.auto_focus) {
            if (state == State.PREVIEW) {
                CameraManager.getInstance(mContext).requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            state = State.SUCCESS;
           // 解析成功，回调
            if (mDecodeListener!=null){
                mDecodeListener.success((Result) message.obj);
            }

        } else if (message.what == R.id.decode_failed) {
            state = State.PREVIEW;
            CameraManager.getInstance(mContext).requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.getInstance(mContext).stopPreview();
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
        removeMessages(R.id.decode);
        removeMessages(R.id.auto_focus);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.getInstance(mContext).requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.getInstance(mContext).requestAutoFocus(this, R.id.auto_focus);
        }
    }

    public void setDecodeListener(decodeListener mDecodeListener) {
        this.mDecodeListener = mDecodeListener;
    }

    private enum State {
        PREVIEW, SUCCESS, DONE
    }



    public interface decodeListener{
        void success(Result result);
    }

}
