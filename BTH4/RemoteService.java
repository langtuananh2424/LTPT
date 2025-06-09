import java.rmi.Remote;
import java.rmi.RemoteException;

// Giao diện từ xa phải kế thừa từ java.rmi.Remote
public interface RemoteService extends Remote {
    // Mỗi phương thức từ xa phải khai báo ném RemoteException
    long calculateSum(int[] numbers) throws RemoteException;
}