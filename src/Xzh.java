import java.util.concurrent.Semaphore;

public class Xzh {
    public static void main(String[] arg) {
        for (int i = 0; i < 3; i++) {
            new Thread(new Producer()).start();
            new Thread(new Consumer()).start();
        }
    }

    static Warehouse buffer = new Warehouse();

    static class Producer implements Runnable {
        public void run() {
            while (true) {
                try {
                    buffer.put();

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class Consumer implements Runnable {
        public void run() {
            while (true) {
                try {
                    buffer.take();

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Warehouse {
        final Semaphore notFull = new Semaphore(10);
        final Semaphore notEmpty = new Semaphore(0);
        final Semaphore mutex = new Semaphore(1);
        int cnt=0;

        public void put() throws InterruptedException {
            notFull.acquire();
            mutex.acquire();
            try {
                cnt++;
                System.out.println("里面：生产产品,现在剩余"+buffer.cnt+"产品");
            } finally {
                mutex.release();
                notEmpty.release();
            }
        }

        public void take() throws InterruptedException {
            notEmpty.acquire();
            mutex.acquire();
            try {
                cnt--;
                System.out.println("里面：消费产品，现在剩余"+buffer.cnt+"产品");
            } finally {
                mutex.release();
                notFull.release();
            }
        }
    }
}