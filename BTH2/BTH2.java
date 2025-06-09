import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BTH2 {
    // Kích thước của cấu trúc dữ liệu chia sẻ
    private static final int N = 200;
    // Số luồng sinh dữ liệu
    private static final int K = 2;
    // Số luồng xử lý dữ liệu
    private static final int H = 3;

    // Cấu trúc dữ liệu chia sẻ: hàng đợi giới hạn kích thước
    private static final ArrayBlockingQueue<Integer> sharedQueue = new ArrayBlockingQueue<>(N);

    // Semaphore để kiểm soát số lượng phần tử có thể ghi và đọc
    private static final Semaphore empty = new Semaphore(N);
    private static final Semaphore full = new Semaphore(0);

    // Đếm số thứ tự luồng sinh và xử lý
    private static final AtomicInteger producerCount = new AtomicInteger(1);
    private static final AtomicInteger consumerCount = new AtomicInteger(1);

    public static void main(String[] args) {
        // Tạo và khởi động các luồng sinh dữ liệu
        for (int i = 0; i < K; i++) {
            new Thread(new Producer(), "P" + producerCount.getAndIncrement()).start();
        }
        // Tạo và khởi động các luồng xử lý dữ liệu
        for (int i = 0; i < H; i++) {
            new Thread(new Consumer(), "C" + consumerCount.getAndIncrement()).start();
        }
    }

    // Luồng sinh dữ liệu
    static class Producer implements Runnable {
        private final Random rand = new Random();

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(rand.nextInt(1000) + 500); // Ngủ 0.5-1.5s
                    int value = rand.nextInt(1000); // Sinh giá trị ngẫu nhiên
                    empty.acquire(); // Chờ nếu hàng đợi đầy
                    sharedQueue.put(value);
                    full.release(); // Báo có phần tử mới
                    System.out.printf("%s: %d %tT%n", Thread.currentThread().getName(), value, new Date());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Luồng xử lý dữ liệu
    static class Consumer implements Runnable {
        private final Random rand = new Random();

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(rand.nextInt(1200) + 400); // Ngủ 0.4-1.6s
                    full.acquire(); // Chờ nếu hàng đợi rỗng
                    int value = sharedQueue.take();
                    empty.release(); // Báo có chỗ trống mới
                    // Xử lý: ví dụ tính bình phương giá trị
                    int result = value * value;
                    System.out.printf("%s: %d - %d - %tT%n", Thread.currentThread().getName(), value, result, new Date());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}