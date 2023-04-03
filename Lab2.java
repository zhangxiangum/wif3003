import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lab2 {
    public static void main(String[] args) throws InterruptedException {

        
        Q1 q1 = new Q1();
        System.out.println("Sequence Time"+q1.getMax());
      

        
        int arr[]= new int[5000000];
        Random random=new Random();
        for (int i=0; i<arr.length; i++){
        arr[i]=random.nextInt(50000)+1;
        }
        // Q2 q2_a=new Q2(arr,0,((int)arr.length/2));
        // q2_a.start();
        // Q2 q2_b=new Q2(arr,((int)arr.length/2),arr.length);
        // q2_b.start();
        // q2_a.join();
        // q2_b.join();
        // System.out.println(Math.max(q2_a.getMax(), q2_b.getMax()));

        Q2_Executor q2=new Q2_Executor(arr, 6); // 
        q2.CoccurentArray();
        System.out.println("Cocurrent Time"+q2.CoccurentArray());

    }

    static class Q1 {
        int arr[] = new int[5000000];
        public Q1() {
        };

        public String getMax() {
            Random random=new Random();
            for(int i =0; i<arr.length;i++){
                arr[i]=random.nextInt(50000)+1;
            }
            int max = arr[0];
            Timer time=new Timer();
            time.start();
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }

            }
            time.end();

            return time.getDuration()+" Max:"+ max;
        };
        
    }

    static class Q2 extends Thread {
        private int[] arr;
        private int start;
        private int end;
        private int max;

        // public static AtomicInteger number = new AtomicInteger(0);
        public Q2(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        };

        @Override
        public void run() {
            max = arr[start];
            // int index=0;
            for (int i = start + 1; i < end; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                    // index=i;
                }
            }
        }

        public int getMax() {
            return max;
        }
    }

    static class Timer {
        private long startTime;
        private long endTime;

        public void start() {
            startTime = System.currentTimeMillis();
        }

        public void end() {
            endTime = System.currentTimeMillis();
        }

        public long getDuration() {
            return endTime - startTime;
        }
    }

    static class Q2_Executor {
        private int[] arr;
        private int num;
        private int max;
        public Q2_Executor(int[] arr,int num) {
            this.arr = arr;
            this.num=num;
        }
        public String CoccurentArray() {
            ExecutorService executorService = Executors.newFixedThreadPool(num);
            max=arr[0];
            Timer timer=new Timer();
            timer.start();
            for (int i = 1; i < arr.length; i++) {
                int index = i;
                executorService.execute(() -> {
                    if(arr[index]>max){
                        max=arr[index];
                    }
                });
            }
           timer.end();
           
            executorService.shutdown();
            return " "+timer.getDuration()+" Max:"+ max;
        }
       
    }
}
