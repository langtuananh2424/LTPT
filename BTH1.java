import java.util.*;
import java.text.SimpleDateFormat;

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
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                System.out.println(threadName + ": " + array[i] + " : " + timeStamp);
            }
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
        final int N = 100;
        final int k = 5;
        int[] array = new int[N];
        Random random = new Random();

        // Sinh mảng ngẫu nhiên
        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(1000);
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

        for (int i = 0; i < k; i++) {
            System.out.println("Sum of T" + (i + 1) + ": " + threads[i].getResult());
        }

        // // Luồng tìm số chính phương
        // System.err.println("Perfect Square Thread");
        // PerfectSquareThread[] squareThreads = new PerfectSquareThread[k];
        // for (int i = 0; i < k; i++) {
        //     int start = i * chunkSize;
        //     int end = (i == k - 1) ? N : start + chunkSize;
        //     squareThreads[i] = new PerfectSquareThread(array, start, end, "T" + (i + 1));
        //     squareThreads[i].start();
        // }
        // for (int i = 0; i < k; i++) {
        //     try {
        //         squareThreads[i].join();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        // for (int i = 0; i < k; i++) {
        //     System.out.println("Perfect squares from T" + (i + 1) + ": " + squareThreads[i].getSquares());
        // }

        // // Luồng tìm số nguyên tố
        // System.err.println("Prime Thread");
        // PrimeThread[] primeThreads = new PrimeThread[k];
        // for (int i = 0; i < k; i++) {
        //     int start = i * chunkSize;
        //     int end = (i == k - 1) ? N : start + chunkSize;
        //     primeThreads[i] = new PrimeThread(array, start, end, "T" + (i + 1));
        //     primeThreads[i].start();
        // }
        // for (int i = 0; i < k; i++) {
        //     try {
        //         primeThreads[i].join();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        // for (int i = 0; i < k; i++) {
        //     System.out.println("Primes from T" + (i + 1) + ": " + primeThreads[i].getPrimes());
        // }
    }
}