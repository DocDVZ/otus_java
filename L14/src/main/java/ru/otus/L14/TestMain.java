package ru.otus.L14;

/**
 * Created by DocDVZ on 23.07.2017.
 */
public class TestMain {

    private Integer i=0;

    public static void main(String[] args) throws Exception{
        final TestMain main = new TestMain();
        Integer k = main.getI();
        while (k<100){

            Thread thread = new Thread(() -> {
                System.out.println(main.getI());
            });
            thread.start();
            Thread.sleep(50);
            main.setI(++k);
            thread.join();
        }
    }

    public int getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }
}
