package app.fileCopier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileInput extends Thread {
    private FileInputStream inputStream;
    private InputStreamReader inputStreamReader;
    private SynchronizedStack<Integer> buffer;
    private FileCopier parent;

    // Parameters are: the stack buffer allocated to this thread, path to the input
    // file, parent of this thread.
    public FileInput(SynchronizedStack<Integer> stack, String path, FileCopier parent) {
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            System.err.println("The file to be copied cannot be found.");
            e.printStackTrace();
            setEndOfStream(true);
            return;
        }
        inputStreamReader = new InputStreamReader(inputStream);
        this.buffer = stack;
        this.parent = parent;
    }

    // Thread runs until the end of the selected file. At the end passes
    // information, that the file is fully read to the parent and to the other
    // thread
    public void run() {

        try {
            while (!parent.isEndOfStream()) {
                fillBuffer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("FileInput thread interrupted.");
            e.printStackTrace();
        } finally {
            try {
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                System.err.println("Error closing FileInput Stream resources.");
                e.printStackTrace();
            }
        }

    }

    // This function fills the stack buffer allocated to this thread with read
    // characters.
    public void fillBuffer() throws IOException, InterruptedException {
        synchronized (buffer) {
            int character;
            while (!buffer.isEmpty()) {
                buffer.wait();
            }
            while (!buffer.isFull() && !parent.isEndOfStream()) {
                character = inputStreamReader.read();
                if (character != -1) {
                    buffer.push(character);
                } else {
                    setEndOfStream(true);
                }
            }
            // We notify the other thread, that the buffer is full.
            buffer.notifyAll();
        }

    }

    // This function informs the parent and the other class, that the input file has
    // been fully read. This means, all that remains to be copied are in the buffer.
    public void setEndOfStream(boolean endOfStream) {
        parent.setEndOfStream(endOfStream);
    }
}
