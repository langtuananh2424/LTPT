import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// Lớp triển khai phải kế thừa từ UnicastRemoteObject
// để cho phép các client từ xa gọi các phương thức của nó
public class RemoteServiceImpl extends UnicastRemoteObject implements RemoteService {

    // Constructor mặc định cho UnicastRemoteObject phải ném RemoteException
    protected RemoteServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public long calculateSum(int[] numbers) throws RemoteException {
        System.out.println("Server đã nhận được yêu cầu tính tổng một mảng.");
        if (numbers == null || numbers.length == 0) {
            System.out.println("Mảng rỗng hoặc null, trả về tổng là 0.");
            return 0;
        }

        // Nhiệm vụ tự định nghĩa: Tính tổng các phần tử trong mảng
        long sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        System.out.println("Tổng của mảng đã tính là: " + sum);
        return sum;
    }
}