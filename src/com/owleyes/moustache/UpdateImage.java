package com.owleyes.moustache;

import android.os.Handler;
import android.util.Log;

public class UpdateImage implements Runnable {

    private CustomRelativeLayout _rl;

    private int _state;

    private int _button;

    private long _currentTime;

    private Handler _handler;

    public UpdateImage(CustomRelativeLayout rl, Handler handler) {
        _rl = rl;
        _handler = handler;
    }

    public void setState(int state, int button) {
        _state = state;
        _currentTime = System.currentTimeMillis();
        _button = button;
    }

    @Override
    public void run() {
        Log.e("Run", "We are running");
        int amount = (int) ((System.currentTimeMillis() - _currentTime) / 100);
        if (_button == Viewer.MINUS) {
            amount = -amount;
        }
        _rl.handleEvent(_state, amount);
        _handler.postDelayed(this, 100);
    }
}
