package com.sparta;

public class Main {

    public static void main(String[] args) {

        try{
            RamenCook ramenCook = new RamenCook(4); // 스레드 4개를 시작합니다. 생성자로 라면의 갯수를 넣어줍니다. (int)
            new Thread(ramenCook,"A").start(); // 가장 먼저 "A" 이름을 가진 라면(쓰레드)이 조리됩니다.
            new Thread(ramenCook,"B").start();
            new Thread(ramenCook,"C").start();
            new Thread(ramenCook,"D").start();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}


class RamenCook implements Runnable{ // Runnable인터페이스를 구현합니다
    // Runnable인터페이스는 자바에서 멀티스레딩을 위해 사용되는 인터페이스 입니다.
    // 단 하나의 run을 정의합니다.

    private int ramenCount;
    private String[] burners = {"_","_","_","_"}; // 비어 있는 버너 4개

    public RamenCook(int count){
        ramenCount = count;
    }

    @Override
    public void run() { // 구현할 run 메서드 정의
        while(ramenCount>0){ // 지금 이 객체의 라면의 갯수가 0보다 많다면 반복합니다.
            synchronized (this){ // 스레드를 여러개 생성했지만 synchronized(this)를 통해 이 객체에 다른 스레드가 접근하지 못하도록 합니다.
                // 이 블록내의 코드가 실행되는 동안 다른 스레드는 이 객체의 synchronized블록에 접근할 수 없습니다.
                // 보통 A쓰레드가 제일 먼저 실해되므로 A쓰레드가 해당 블록 안에 진입하면 다른 블록은 진입하지 못합니다. ( 근데 딱한번 B쓰레드가 먼저 들어갔습니다..? 캡쳐를 못했네요)
                // 다른 쓰레드들은 대기 상태에 들어갑니다.
                ramenCount--;
                System.out.println(
                        Thread.currentThread().getName()+": "+ramenCount+"개 남음");

                for(int i=0;i<burners.length;i++){
                    if(!burners[i].equals("_")) continue;;

                    synchronized (this) { // 이미 synchronized블록 안이므로 이중으로 쓰지 않아도 같은 결과가 나올 것입니다.

                        burners[i] = Thread.currentThread().getName();
                        System.out.println(
                                "               "
                                        + Thread.currentThread().getName()
                                        + ": [" + (i + 1) + "]번 버너 ON");
                        showBurners();

                    }
                    try {
                        Thread.sleep(2000); // 2초 동안 대기합니다.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void showBurners(){ // 단순히 출력하는 메서드입니다.
        String stringToPrint
                = "                          ";
        for(int i=0;i<burners.length;i++){
            stringToPrint += (" " + burners[i]);
        }
        System.out.println(stringToPrint);
    }
}