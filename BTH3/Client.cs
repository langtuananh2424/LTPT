using System;
using System.Net.Sockets;
using System.Text;
using System.Linq;
using System.Collections.Generic;

public class Client
{
    private const string SERVER_ADDRESS = "172.20.10.14"; // Thay thế bằng Địa chỉ IP thực của máy chủ Java
    private const int SERVER_PORT = 12345;                // Cổng của Server Java
    private const int N_ELEMENTS = 150;                   // Số lượng phần tử trong mảng (N > 100)

    public static void Main(string[] args)
    {
        TcpClient client = null;
        NetworkStream stream = null;
        try
        {
            // 1. Kết nối tới Server Java
            client = new TcpClient(SERVER_ADDRESS, SERVER_PORT);
            Console.WriteLine($"Client C# đã kết nối tới Server Java {SERVER_ADDRESS}:{SERVER_PORT}");

            stream = client.GetStream(); // Lấy luồng mạng để gửi/nhận dữ liệu

            // 2. Tạo mảng A ngẫu nhiên
            Random random = new Random();
            int[] A = new int[N_ELEMENTS];
            for (int i = 0; i < N_ELEMENTS; i++)
            {
                A[i] = random.Next(1000); // Sinh số ngẫu nhiên từ 0 đến 999
            }
            Console.WriteLine($"Client C# đã sinh mảng A ({N_ELEMENTS} phần tử): [{string.Join(", ", A)}]");

            // 3. Chuyển mảng A thành chuỗi và gửi cho server
            // Các phần tử phân tách bằng khoảng trắng
            string arrayAsString = string.Join(" ", A);
            
            // Chuyển chuỗi thành mảng byte để gửi qua mạng (sử dụng UTF-8)
            byte[] data = Encoding.UTF8.GetBytes(arrayAsString + "\n"); // Thêm ký tự xuống dòng để Server Java có thể đọc bằng readLine()

            stream.Write(data, 0, data.Length);
            Console.WriteLine("Client C# đã gửi mảng A tới Server Java.");

            // 4. Nhận kết quả từ Server và hiển thị
            byte[] buffer = new byte[256]; // Buffer để đọc dữ liệu từ server
            int bytesRead = stream.Read(buffer, 0, buffer.Length);
            string serverResponse = Encoding.UTF8.GetString(buffer, 0, bytesRead);
            Console.WriteLine($"Client C# nhận được từ Server Java: {serverResponse}");

        }
        catch (SocketException e)
        {
            Console.WriteLine($"Lỗi Socket: {e.Message}");
        }
        catch (IOException e)
        {
            Console.WriteLine($"Lỗi I/O: {e.Message}");
        }
        catch (Exception e)
        {
            Console.WriteLine($"Lỗi không xác định: {e.Message}");
        }
        finally
        {
            // Đóng luồng và client
            if (stream != null) stream.Close();
            if (client != null) client.Close();
            Console.WriteLine("Kết nối đã đóng.");
        }
        Console.WriteLine("Nhấn phím bất kỳ để thoát...");
        Console.ReadKey();
    }
}