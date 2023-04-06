package activitytracker.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WorkerListener extends Thread {

    private ServerSocket serverSocket;
    private ArrayList<WorkerHandlerThread> workerThreads;
    private int workerID;
    private boolean running;
    private final Object workerThreadsListLock = new Object();

    public WorkerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        workerThreads = new ArrayList<>();
    }

    public ArrayList<WorkerHandlerThread> getWorkerThreads() {
        synchronized (workerThreadsListLock) {
            return workerThreads;
        }
    }

    @Override
    public void run() {
        try {
            startListening();
            while (running) {
                // Accept worker connection
                Socket workerSocket = this.serverSocket.accept();
                System.out.println("[Server] Worker node added");

                // Handle the new connection
                WorkerHandlerThread workerThread = new WorkerHandlerThread(workerSocket);
                workerThread.start();
                synchronized (workerThreadsListLock) {
                    workerThreads.add(workerThread);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        this.running = true;
    }

    public void stopListening() {
        System.out.println("[Server] Shutting down worker listener");
        this.running = false;
    }

}
