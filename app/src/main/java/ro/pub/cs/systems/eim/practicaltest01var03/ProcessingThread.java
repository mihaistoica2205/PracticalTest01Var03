package ro.pub.cs.systems.eim.practicaltest01var03;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import java.util.Date;
import java.util.Random;


public class ProcessingThread extends Thread {
    final public static String LEFT_COUNT = "leftCount";
    final public static String RIGHT_COUNT = "rightCount";

    final public static String NUMBER_OF_CLICKS = "numberOfClicks";
    final public static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;

    final public static String EQUATION1 = "firstNumber";
    final public static String EQUATION2 = "secondNumber";

    final public static String[] actionTypes = {
            "ro.pub.cs.systems.eim.practicaltest01.sum",
            "ro.pub.cs.systems.eim.practicaltest01.diff"
    };

    final public static int NUMBER_OF_CLICKS_THRESHOLD = 5;
    final public static int SERVICE_STOPPED = 0;
    final public static int SERVICE_STARTED = 1;

    final public static String PROCESSING_THREAD_TAG = "[Processing Thread]";

    final public static String BROADCAST_RECEIVER_EXTRA = "message";
    final public static String BROADCAST_RECEIVER_TAG = "[Message]";
    private final Context context;
    private boolean isRunning = true;

    private final Random random = new Random();

    private final String equation1;
    private final String equation2;

    public ProcessingThread(Context context, String equation1, String equation2) {
        this.context = context;

        this.equation1 = equation1;
        this.equation2 = equation2;
    }

    @Override
    public void run() {
        Log.d(PROCESSING_THREAD_TAG, "Thread has started! PID: " + Process.myPid() + " TID: " + Process.myTid());
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d(PROCESSING_THREAD_TAG, "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        String action = actionTypes[random.nextInt(actionTypes.length)];
        intent.setAction(action);
        if (action.equals("ro.pub.cs.systems.eim.practicaltest01.sum")) {
            intent.putExtra(BROADCAST_RECEIVER_EXTRA, "Equation 1: " + equation1);
        } else if (action.equals("ro.pub.cs.systems.eim.practicaltest01.diff")) {
            intent.putExtra(BROADCAST_RECEIVER_EXTRA, "Equation 2: " + equation2);
        }

        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}