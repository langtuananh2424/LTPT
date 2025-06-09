import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345; // Cổng mà server sẽ lắng nghe

    public static void main(String[] args) {
        // Tạo một Thread Pool để xử lý nhiều client đồng thời
        // fixedThreadPool với 5 luồng nghĩa là có thể xử lý tối đa 5 client cùng lúc
        ExecutorService executor = Executors.newFixedThreadPool(5); 

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server dang lang nghe tren cong " + PORT + "...");

            while (true) {
                // Chấp nhận một kết nối mới từ client
                Socket clientSocket = serverSocket.accept(); 
                System.out.println("Client da ket noi tu: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                // Giao nhiệm vụ xử lý client này cho một luồng trong thread pool
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Loi Server: " + e.getMessage());
        } finally {
            // Đóng thread pool khi server kết thúc (ví dụ: khi có lỗi hoặc server bị dừng thủ công)
            executor.shutdown(); 
        }
    }

    // Lớp nội bộ này sẽ được thực thi bởi mỗi luồng trong thread pool để xử lý từng client
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                // Tạo luồng đọc từ client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Tạo luồng ghi tới client (true để tự động flush)
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                // Đọc dữ liệu mảng A dưới dạng chuỗi từ client
                String clientMessage = in.readLine();
                if (clientMessage != null) {
                    System.out.println("Server nhan duoc tu Client " + clientSocket.getInetAddress().getHostAddress() + ": " + clientMessage.substring(0, Math.min(clientMessage.length(), 50)) + "..."); // Chỉ in 50 ký tự đầu

                    // Phân tích chuỗi nhận được thành mảng các số nguyên
                    int[] numbers = Arrays.stream(clientMessage.split(" "))
                                          .mapToInt(Integer::parseInt)
                                          .toArray();

                    // Thực hiện nhiệm vụ tự định nghĩa: Tìm giá trị lớn nhất trong mảng
                    int max_value = findMaxValue(numbers);

                    // Gửi kết quả về cho client
                    System.out.println("Gia tri lon nhat trong mang la: " + max_value);
                    System.out.println("Server da gui ket qua ve Client " + clientSocket.getInetAddress().getHostAddress() + ": " + max_value);
                }

            } catch (IOException e) {
                System.err.println("Lỗi xử lý Client " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " - " + e.getMessage());
            } finally {
                try {
                    // Luôn đóng socket client sau khi hoàn tất xử lý
                    clientSocket.close(); 
                    System.out.println("Da dong ket noi toi Client " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
                } catch (IOException e) {
                    System.err.println("Loi khi dong socket client: " + e.getMessage());
                }
            }
        }

        // Phương thức tìm giá trị lớn nhất trong mảng
        private int findMaxValue(int[] arr) {
            if (arr == null || arr.length == 0) {
                return Integer.MIN_VALUE; // Trả về giá trị nhỏ nhất nếu mảng rỗng
            }
            int max = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }
            }
            return max;
        }
    }
}