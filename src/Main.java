import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Item item = new Item();
        //3个生产者进程
        new Thread(new Producer(item)).start();
        new Thread(new Producer(item)).start();
        new Thread(new Producer(item)).start();
        //4个消费者进程
        new Thread(new Consumer(item)).start();
        new Thread(new Consumer(item)).start();
        new Thread(new Consumer(item)).start();
        new Thread(new Consumer(item)).start();
    }
}
// 生产者线程
class Producer implements Runnable{
    private Item item;

    public Producer(Item item) {
        this.item = item;
    }

    @Override
    public void run() {
        while (true){
            item.set();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
//消费者线程
class Consumer implements Runnable{
    private Item item;

    public Consumer(Item item) {
        this.item = item;
    }

    @Override
    public void run() {
        while (true){
            item.get();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Item{
    //缓存区
    private int[] buf = new int[20];
    //信号量，缓存区剩余情况
    private int empty = 20;
    //in，out为生产和消费资源的指针
    private int in=0,out=0;
    //生产资源
    public synchronized void set(){
        //没有空间可以存放生产时进入等待
        if (empty <=0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buf[in] = in+1;
        System.out.println("生产"+buf[in]);
        System.out.println(Arrays.toString(buf));
        empty--;
        in = (in+1)%20;
        notify();//唤醒等待进程
    }
    //消费资源
    public synchronized void get(){
        //空余20个说明没有资源可以消费，进入等待状态
        if (empty>=20){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("消费"+buf[out]);
        buf[out] = 0;
        System.out.println(Arrays.toString(buf));
        empty++;
        out = (out+1)%20;
        notify();//唤醒等待进程
    }
}
