package streams.tasks;

import javafx.concurrent.Task;

/**
 * Created by mikhail.davydov on 2016/7/29.
 */
public abstract class CustomTask extends Task {
    private boolean isPaused = false;
    private boolean isStopped = false;
    private boolean run = true;

    public boolean getRun() {
        return run;
    }

    public void setRun(boolean running) {
        run = running;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void stopThread() {
        isStopped = true;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
        synchronized(this) { this.notifyAll(); }
    }

    public synchronized void checkPaused() {
        while(isPaused) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                this.stopThread();
                this.cancel();
            }
        }
    }
}
