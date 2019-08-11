package services.exc;

public class ServiceNotAvailableException extends Exception {
    public ServiceNotAvailableException() {
        super();
    }
    public ServiceNotAvailableException(String message) {
        super(message);
    }
}
