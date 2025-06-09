import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Random; // Để in mảng dễ nhìn

public class RmiClient {

    private static final String SERVER_ADDRESS = "172.20.10.14"; // Địa chỉ IP của Server RMI
    private static final int PORT = 23456;                   // Cổng của RMI Registry
    private static final int N_ELEMENTS = 10;               // Số lượng phần tử trong mảng test

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_ADDRESS, PORT);

            // Tra cứu đối tượng từ xa bằng tên mới
            RemoteService service = (RemoteService) registry.lookup("RemoteSumService");

            System.out.println("Client RMI da ket noi va tim thay doi tuong tu xa.");

            // Sinh ra một mảng số nguyên ngẫu nhiên
            int[] numbersToSend = new int[N_ELEMENTS];
            Random random = new Random();
            for (int i = 0; i < N_ELEMENTS; i++) {
                numbersToSend[i] = random.nextInt(100); // Số ngẫu nhiên từ 0 đến 99
            }
            System.out.println("Client sinh mang de gui: " + Arrays.toString(numbersToSend));

            try {
                // Gọi phương thức từ xa calculateSum() với mảng đã sinh
                long sumResult = service.calculateSum(numbersToSend);

                // Hiển thị kết quả
                System.out.println("Tong nhan duoc tu Server: " + sumResult);
            } catch (RemoteException e) {
                System.err.println("Loi khi goi phuong thuc tu xa: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Loi Client RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}