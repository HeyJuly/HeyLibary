package com.hey.scan.camera;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

    private final CountDownLatch handlerInitLatch;
    private Handler handler;
	private CaptureActivityHandler decodeHandler;
	private Context mContext;
	DecodeThread(CaptureActivityHandler handler, Context context) {
		this.decodeHandler = handler;
		mContext=context;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(decodeHandler,mContext);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
