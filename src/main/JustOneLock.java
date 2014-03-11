package main;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * Classe che è in grado di far partire una sola istanza di un software.<br>
 * E' quindi possibile eseguire una sola istanza di ADISys 
*/
public class JustOneLock {
    private String appName;
    private File file;
    private FileChannel channel;
    private FileLock lock;

    public JustOneLock(String appName) {
        this.appName = appName;
    }

    public boolean isAppActive() {
        try {
            file = new File(System.getProperty("user.home"), appName + ".tmp");
            channel = new RandomAccessFile(file, "rw").getChannel();
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                // il file è già loccato
                closeLock();
                return true;
            }

            if (lock == null) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    // distrugge il lock quando la JVM viene chiusa
                    public void run() {
                        closeLock();
                        deleteFile();
                    }
                });
            return false;
        }
        catch (Exception e) {
            closeLock();
            return true;
        }
    }

    // metodo per chiudere il lock sul file
    private void closeLock() {
        try { 
            lock.release();  
        }catch (Exception e) {  
            
        }
        try { 
            channel.close(); 
        }
        catch (Exception e) {  
        }
    }
    
    // metodo per cancellare il file di lock
    private void deleteFile() {
        try { 
            file.delete(); 
        }
        catch (Exception e) { 
        }
    }
}
