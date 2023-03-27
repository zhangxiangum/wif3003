import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Lab2{
    public static void main(String[] args) throws InterruptedException {
        // Q1 q1=new Q1();
        // System.out.println(q1.FindLargestNumber());

        int arr[]= new int[1000000];
        Random random=new Random();
        for (int i=0; i<arr.length; i++){
            arr[i]=random.nextInt(50000)+1;
        }
        Q2 q2_a=new Q2(arr,0,((int)arr.length/2));
        q2_a.start();
        Q2 q2_b=new Q2(arr,((int)arr.length/2),arr.length);
        q2_b.start();

        q2_a.join();
        q2_b.join();
        System.out.print(Math.max(q2_a.getMax(), q2_b.getMax()));

    }

static class Q1{
    int arr[]= new int[1000000];
    public Q1(){};
    public String FindLargestNumber(){
        int max=arr[0];
        int index=0;
        for(int i=1; i<arr.length;i++){
            if (arr[i]>max){
                max=arr[i];
                index=i;
            }

        }
        return "No: "+index+"; Value: "+max;
    };
}

static class Q2 extends Thread{
    private int[] arr;
    private int start;
    private int end;
    private int max;
    // public static AtomicInteger number = new AtomicInteger(0);
    public Q2(int[] arr,int start,int end){
        this.arr=arr;
        this.start=start;
        this.end=end;
    };
    @Override
    public void run(){
            max=arr[start];
            // int index=0;
            for(int i=start+1; i<end;i++){
                if (arr[i]>max){
                    max=arr[i];
                    // index=i;
                }
            }
    }
    public int getMax(){
        return max;
    }

    // public String FindLargestNumber(){
    //     Random random=new Random();
    //     for (int i=0; i<arr.length; i++){
    //         arr[i]=random.nextInt(50000)+1;
    //     }
    //     int max=arr[0];
    //     int index=0;
    //     for(int i=1; i<arr.length;i++){
    //         if (arr[i]>max){
    //             max=arr[i];
    //             index=i;
    //         }

    //     }
    //     return "No: "+index+"; Value: "+max;
    // };
}
}

