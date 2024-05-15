Проект «Часы». В проекте должен быть реализован класс, который дает возможность пользоваться часами со стрелками так же, как и цифровыми часами. В классе «Часы со стрелками» хранятся повороты стрелок.
Для реализации проекта "Часы" можно применить паттерн проектирования "Наблюдатель" (Observer pattern).
import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(int hours, int minutes, int seconds);
}

class Clock {
    private List<Observer> observers = new ArrayList<>();
    private int hours;
    private int minutes;
    private int seconds;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void setTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        notifyObservers();
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(hours, minutes, seconds);
        }
    }
}

class DigitalClock implements Observer {
    @Override
    public void update(int hours, int minutes, int seconds) {
        System.out.printf("Цифровые часы: %02d:%02d:%02d\n", hours, minutes, seconds);
    }
}

class AnalogClock implements Observer {
    @Override
    public void update(int hours, int minutes, int seconds) {
        System.out.println("Стрелочные часы: " + hours + " часы, " + minutes + " минуты, " + seconds + " секунды");
    }
}

public class Main {
    public static void main(String[] args) {
        Clock clock = new Clock();
        DigitalClock digitalClock = new DigitalClock();
        AnalogClock analogClock = new AnalogClock();

        clock.addObserver(digitalClock);
        clock.addObserver(analogClock);
        
        clock.setTime(12, 30, 0);
    }
}
