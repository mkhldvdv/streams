package streams.tasks;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mikhail.davydov on 2016/7/28.
 */
public class Tasks {

    private AtomicInteger current = new AtomicInteger();
    private final LinkedBlockingQueue<Integer> buffer;
    private AtomicBoolean dataExist = new AtomicBoolean(true);


    public Tasks(int capacity) {
        buffer = new LinkedBlockingQueue<>(capacity);
    }

    /**
     * create task for input stream
     *
     * @param input file destination
     * @return created task
     */
    public CustomTask createIn(String input) {
        return new CustomTask() {
            @Override
            protected Void call() throws Exception {
                InputStream is = null;
                try {
                    is = new FileInputStream(input);
                    while (is.available() > 0) {
                        // check if the thread paused
                        checkPaused();

                        buffer.put(is.read());
                        current.incrementAndGet();

                        if (is.available() == 0) {
                            dataExist.set(false);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    dataExist.set(false);
                    is.close();
                }

                return null;
            }
        };
    }

    /**
     * create task for output stream
     *
     * @param output file destination
     * @return created task
     */
    public CustomTask createOut(String output) {
        return new CustomTask() {
            @Override
            protected Void call() throws Exception {
                OutputStream os = null;
                try {
                    os = new FileOutputStream(output);
                    while (!buffer.isEmpty() || dataExist.get()) {
                        // check if the thread paused
                        checkPaused();

                        os.write(buffer.take());
                        current.getAndDecrement();
                    }
                } finally {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                }

                return null;
            }
        };
    }

    /**
     * create task to monitor buffer fullness
     *
     * @param capacity buffer capacity
     * @return created task
     */
    public CustomTask monitorState(int capacity) {
        return new CustomTask() {
            @Override
            protected Void call() throws Exception {

                while (!isStopped()) {
                    updateProgress(current.get(), capacity);
                }

                return null;
            }
        };
    }
}
