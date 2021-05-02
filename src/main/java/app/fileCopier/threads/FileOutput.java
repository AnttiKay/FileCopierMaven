package app.fileCopier.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import app.fileCopier.SynchronizedStack;


public class FileOutput extends Thread {
    private FileOutputStream outputStream;
    private OutputStreamWriter outputStreamWriter;
    private SynchronizedStack<Integer> buffer;
    private String path = "";
    private boolean endOfStream = false;

    // Parameters are: the stack buffer allocated to this thread, path to the input
    // file, parent of this thread. Thread stops as it finds -1 in the stack to signify end of stream.
    public FileOutput(SynchronizedStack<Integer> stack, String path) {
        if (stack == null || path.equals("")) {
            setEndOfStream(true);
            throw new IllegalArgumentException();
        } else {
            this.path = path;
            this.buffer = stack;
        }

    }

    // Thread runs until the input file has been read. Every time the buffer is
    // filled by the reader thread, this thread writes the buffers contents to file
    // specified as the output file
    public void run() {
        try {
            while (!endOfStream) {
                writeBuffer();
            }
            // To catch the final time, when buffer may be partially filled.
        } catch (InterruptedException e) {
            System.err.println("FileOutput thread interrupted.");
            e.printStackTrace();
        }

    }

    // This function writes the contents of the stack buffer into the selected
    // output file.
    public void writeBuffer() throws InterruptedException {
        synchronized (buffer) {
            // We wait until the other thread notifies this one.
            // We check the first character of the buffer, if it is -1 then it is end of the
            // stream, as otherwise the buffer contains only positive integers. We also
            // remove the end of the line character from the buffer.
            while (!buffer.isFull() && !endOfStream) {
                buffer.wait();

                if (buffer.size() > 0) {
                    if(buffer.peek() == -1){
                        setEndOfStream(true);
                        buffer.pop();
                    }
                }
            }

            int[] array = new int[buffer.size()];

            // We reverse the order of the elements from the stack as they are in reverse
            // order.
            for (int i = (buffer.size() - 1); i > -1; i--) {
                array[i] = buffer.pop();
                //System.out.print(array[i]);
            }

            try {
                // Opening FileOutputStream always overwrites
                // the opened file so we use additional parameter to
                // append instead.
                outputStream = new FileOutputStream(new File(path), true);
                outputStreamWriter = new OutputStreamWriter(outputStream);
                
                // We append the characters to the outputStream file.
                for (int c : array) {
                    outputStreamWriter.write(c);
                }

            } catch (IOException e) {
                System.err.println("FileOutput error writing charaters to file.");
                e.printStackTrace();
            } finally {
                try {
                    outputStreamWriter.close();
                    outputStream.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileOutput stream resources.");
                    e.printStackTrace();
                }

            }
            // We notify the other thread, that the buffer is empty.
            buffer.notifyAll();

        }

    }

    public void setEndOfStream(boolean endofStream) {
        this.endOfStream = endofStream;
    }
}
