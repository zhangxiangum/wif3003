public class Lab1{
public static void main(String[] args) {
    //q1: PrintChar pc=new PrintChar('a');
    //q2: PrintNum pn=new PrintNum(1);
    Driver dr=new Driver();
    dr.run();
    // pc.run();
    // pn.run();

    
}
static class PrintChar extends Thread{
    char ch;
   public PrintChar(char ch) {
    this.ch=ch;
   }
   @Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println(this.ch+":"+i);
		}
	}
}
static class PrintNum extends Thread{
    Integer num;
   public PrintNum(int num) {
    this.num=num;
   }
   @Override
	public void run() {
		for (int i = 1; i < 46; i++) {
			System.out.println(this.num+":"+i);
		}
	}
}
static class Driver {
    public Driver(){}
    public void run() {
        PrintChar pc1=new PrintChar('a');
        //2: PrintChar pc2=new PrintChar('b');
        PrintNum pn=new PrintNum(1);

        pc1.start();
        // pc2.start();
        pn.start();

    }
}

}
