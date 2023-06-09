package tool.common;

public interface Observable {
    void addObserver(Observable.Observer o);
    void removeObserver(Observable.Observer o);
    void notifyObservers();

    public static interface Observer {
        void update(Observable o);
    }
}


