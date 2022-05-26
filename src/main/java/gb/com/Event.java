package gb.com;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Event {

    private final int CARS_COUNT = 4;
    private final CountDownLatch finish = new CountDownLatch(CARS_COUNT);
    private final CountDownLatch ready = new CountDownLatch(CARS_COUNT);
    private final Semaphore smp = new Semaphore(CARS_COUNT / 2);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);

    public void start() {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(smp), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier, ready, finish);
        }
        for (Car car : cars) {
            new Thread(car).start();
        }
        try {
            ready.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            finish.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
