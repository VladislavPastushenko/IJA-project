package tool.common;

import java.util.List;
import java.util.ArrayList;

public abstract class AbstractObservableField implements CommonField {
    List<Observable.Observer> observers;

    public AbstractObservableField() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observable.Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observable.Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observable.Observer observer : observers) {
            observer.update(this);
        }
    }
}
