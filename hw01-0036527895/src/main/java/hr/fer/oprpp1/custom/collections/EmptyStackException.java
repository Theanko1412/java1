package hr.fer.oprpp1.custom.collections;

public class EmptyStackException extends RuntimeException{

    /**
     * Exception is thrown if the stack is empty when method pop is called
     * @param message to be displayed
     */
    public EmptyStackException(String message) {
        super(message);
    }
}
