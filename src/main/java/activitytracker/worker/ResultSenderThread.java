package main.java.activitytracker.worker;

import main.java.activitytracker.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectOutputStream;

class ResultSenderThread extends Thread {
    private boolean sendingResultsBack;

    private final ObjectOutputStream outputStream;
    private final Object MSG_Q_OUT_LOCK;
    private final FifoQueue<Map.WorkerResult> messageQueueOut;

    public ResultSenderThread(ObjectOutputStream outputStream, FifoQueue<Map.WorkerResult> messageQueueOut, Object MSG_Q_OUT_LOCK) {
        this.outputStream = outputStream;
        this.MSG_Q_OUT_LOCK = MSG_Q_OUT_LOCK;
        this.messageQueueOut = messageQueueOut;
        this.sendingResultsBack = true;
    }

    @Override
    public void run() {
        while (sendingResultsBack) {
            synchronized (MSG_Q_OUT_LOCK) {
                while (messageQueueOut.isEmpty()) {
                    try {
                        MSG_Q_OUT_LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Map.WorkerResult resultToSendBack = messageQueueOut.dequeue();
                    outputStream.writeObject(resultToSendBack);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}