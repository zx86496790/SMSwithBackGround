package com.zsen.cameratext;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.*;

public class MyView extends SurfaceView implements  android.view.SurfaceHolder.Callback {

	SurfaceHolder sfh;
	Camera myCamera;
  //  private boolean drawFlag = true;
   // private Canvas canvas;

    public MyView(Context context) {
            super(context);
            //init();
            sfh = getHolder();// ���surfaceHolder����
            sfh.addCallback(this);
            sfh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// ��������
    }

    public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                    int height) {
    	myCamera.setDisplayOrientation(90);
    	myCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            //th.start();
            if (myCamera == null) {
    			myCamera = Camera.open();// �������,���ܷ��ڹ��캯���У���Ȼ������ʾ����.
    			try {
    				myCamera.setPreviewDisplay(holder);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            //drawFlag = false;
            myCamera.stopPreview();// ֹͣԤ��
    		myCamera.release();// �ͷ������Դ
    		myCamera = null;
    }
}
