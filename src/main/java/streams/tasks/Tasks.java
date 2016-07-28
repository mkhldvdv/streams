package streams.tasks;

import javafx.concurrent.Task;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mikhail.davydov on 2016/7/28.
 */
public class Tasks {

    private AtomicInteger current = new AtomicInteger();
    private final LinkedBlockingQueue<Integer> buffer;
    private boolean dataExist = true;

    public Tasks(int capacity) {
        buffer = new LinkedBlockingQueue<>(capacity);
    }

    public Task createIn(String input, int capacity) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                InputStream is = null;
                try {
                    is = new FileInputStream(input);
                    while (is.available() > 0) {
                        buffer.put(is.read());
                        current.incrementAndGet();
                        updateProgress(current.get(), capacity);
                        if (is.available() == 0) {
                            dataExist = false;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    dataExist = false;
                    is.close();
                }

                return null;
            }
        };
    }

    public Task createOut(String output) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                OutputStream os = null;
                try {
                    os = new FileOutputStream(output);
                    while (dataExist) {
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
}
