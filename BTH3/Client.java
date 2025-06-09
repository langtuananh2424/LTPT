import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Client {
    private static final String SERVER_ADDRESS = "172.20.10.14"; // Địa chỉ IP của Server
    private static final int SERVER_PORT = 12345;           // Cổng của Server
    private static final int N_ELEMENTS = 150;              // Số lượng phần tử trong mảng (N > 100)

    public static void main(String[] args) {
        try (
            // Tạo một Socket để kết nối tới Server
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            // Tạo luồng ghi dữ liệu ra Server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Tạo luồng đọc dữ liệu từ Server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Client đã kết nối tới Server " + SERVER_ADDRESS + ":" + SERVER_PORT);

            // 1. Khai báo và sinh ngẫu nhiên mảng A
            int[] A = new int[N_ELEMENTS];
            Random random = new Random();
            for (int i = 0; i < N_ELEMENTS; i++) {
                A[i] = random.nextInt(1000); // Sinh số ngẫu nhiên từ 0 đến 999
            }
            System.out.println("Client đã sinh mảng A (" + N_ELEMENTS + " phần tử): " + Arrays.toString(A));

            // 2. Chuyển mảng A thành chuỗi, các phần tử phân tách bằng khoảng trắng
            String arrayAsString = Arrays.stream(A)
                                         .mapToObj(String::valueOf) // Chuyển từng số thành String
                                         .collect(Collectors.joining(" ")); // Nối các String bằng khoảng trắng

            // Gửi chuỗi mảng A tới Server
            out.println(arrayAsString);
            System.out.println("Client đã gửi mảng A tới Server.");

            // 4. Nhận kết quả từ Server và hiển thị ra màn hình
            String serverResponse = in.readLine();
            System.out.println("Client nhận được từ Server: " + serverResponse);

        } catch (UnknownHostException e) {
            System.err.println("Lỗi: Không tìm thấy Server có địa chỉ " + SERVER_ADDRESS + ": " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi I/O khi kết nối hoặc giao tiếp với Server: " + e.getMessage());
        }
    }
}