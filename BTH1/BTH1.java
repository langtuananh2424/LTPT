import java.text.SimpleDateFormat;
import java.util.*;

public class BTH1 {
    static class SumThread extends Thread {
        private int[] array;
        private int result = 0;
        private int start, end;
        private String threadName;

        public SumThread(int[] array, int start, int end, String threadName) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.threadName = threadName;
        }

        public int getResult() {
            return result;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                result += array[i];
                // String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                // System.out.println(threadName + ": " + array[i] + " : " + timeStamp);
            }
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            System.out.println(threadName + ": " + result + " : " + timeStamp);
        }
    }

    static class PerfectSquareThread extends Thread {
        private int[] array;
        private List<Integer> squares = new ArrayList<>();
        private int start, end;
        private String threadName;

        public PerfectSquareThread(int[] array, int start, int end, String threadName) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.threadName = threadName;
        }

        public List<Integer> getSquares() {
            return squares;
        }

        private boolean isPerfectSquare(int num) {
            int sqrt = (int) Math.sqrt(num);
            return (sqrt * sqrt == num);
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                if (isPerfectSquare(array[i])) {
                    squares.add(array[i]);
                    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    System.out.println(threadName + ": " + array[i] + " : " + timeStamp);
                }
            }
        }
    }

    static class PrimeThread extends Thread {
        private int[] array;
        private List<Integer> primes = new ArrayList<>();
        private int start, end;
        private String threadName;

        public PrimeThread(int[] array, int start, int end, String threadName) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.threadName = threadName;
        }

        public List<Integer> getPrimes() {
            return primes;
        }

        private boolean isPrime(int num) {
            if (num < 2 ) return false;
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) return false;
            }
            return true;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                if (isPrime(array[i])) {
                    primes.add(array[i]);
                    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    System.out.println(threadName + ": " + array[i] + " : " + timeStamp);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhap so phan tu N: ");
        int N = scanner.nextInt();

        System.out.print("Nhap so luong luong k: ");
        int k = scanner.nextInt();

        int[] array = new int[N];
        Random random = new Random();

        // Sinh mảng ngẫu nhiên
        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(10);
        }

        int chunkSize = N / k;

        // In mảng
        System.out.println("Array: " + Arrays.toString(array));

        // Luồng tính tổng
        System.err.println("Sum Thread");
        SumThread[] threads = new SumThread[k];
        for (int i = 0; i < k; i++) {
            int start = i * chunkSize;
            int end = (i == k - 1) ? N : start + chunkSize;
            threads[i] = new SumThread(array, start, end, "T" + (i + 1));
            threads[i].start();
        }

        for (int i = 0; i < k; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // for (int i = 0; i < k; i++) {
        //     System.out.println("Tong cua luong T" + (i + 1) + ": " + threads[i].getResult());
        // }

        int totalSum = 0;
        for (int i = 0; i < k; i++) {
            totalSum += threads[i].getResult();
        }
        System.out.println("Tong cua tat ca cac luong: " + totalSum);

    }
}