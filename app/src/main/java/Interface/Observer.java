package Interface;

public interface Observer<T> {
    void onObserve(int event, T eventMessage);
}
