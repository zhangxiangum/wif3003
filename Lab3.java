import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lab3 {

    static class Room {
        private AtomicInteger GuestNum = new AtomicInteger(0);
        private boolean Cleaning;

        public Room() {
        };

        public synchronized void GuestEnter() throws InterruptedException{
            while (this.GuestNum.get() >= 6) {
                wait();
            }
            this.GuestNum.incrementAndGet();
        }

        public synchronized void CleanerEnter() {
            this.Cleaning = true;
        }

        public synchronized void CleanerLeave() {
            this.Cleaning = false;
        }

        public synchronized void GuestLeave() {
            this.GuestNum.decrementAndGet();
            notifyAll();
        }

        public AtomicInteger getGuestNum() {
            return GuestNum;
        }

        public boolean isCleaning() {
            return Cleaning;
        }

    }

    static class Bank {
        private double account;
        private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

        private final Lock readLock = rwl.readLock();

        private final Lock writeLock = rwl.writeLock();

        public Bank() {
        }

        public  double check() {
            readLock.lock();
            try {
                System.out.println(account);
            } catch (Exception e) {
            }finally{readLock.unlock();}
            return this.account;
        }
        public  double deposit(Double transaction) {
            writeLock.lock();
            try {
                this.account = account + transaction;
            } catch (Exception e) {
            }finally{writeLock.unlock();}
              return transaction;
        }

        public  double withdraw(Double transaction) {
            writeLock.lock();
            try {
                this.account = account - transaction;
            } catch (Exception e) {
            }finally{writeLock.unlock();}
            return transaction;
        }

    }

    public static void main(String[] args) throws InterruptedException,IllegalMonitorStateException {

        // 1
        Room room = new Room();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    if (random.nextInt(2) == 0) { // Guest enter
                        synchronized (room) {
                            try { // thread for guest

                                while (room.isCleaning() == true) {
                                    room.wait();
                                };
                                room.GuestEnter();
                                System.out
                                        .println("Guest " + Thread.currentThread().getName() + " Entered and staying");
                                Thread.sleep(1000);
                                room.GuestLeave();
                                System.out.println("Guest " + Thread.currentThread().getName() + " Leaving");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        synchronized (room) {
                            try { // Cleaning
                                while (room.getGuestNum().get() > 0) {
                                    room.wait();
                                };
                                room.CleanerEnter();
                                System.out.println(
                                        "Cleaner " + Thread.currentThread().getName() + " Entered and Cleaning");
                                Thread.sleep(1000);
                                room.CleanerLeave();
                                System.out.println("Cleaner " + Thread.currentThread().getName() + " Leaving");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.start();
        }

        // 2
        Bank account = new Bank();

        Thread depositThread1 = new Thread(() -> {
            account.deposit(100.00);
        }, "User1-Deposit");

        Thread depositThread2 = new Thread(() -> {
            account.deposit(200.00);
        }, "User2-Deposit");

        // Thread withdrawThread1 = new Thread(() -> {
        //     account.check();
        // }, "User1-Withdraw");

        depositThread1.run();
        depositThread2.run();
        account.check();
    }
}
