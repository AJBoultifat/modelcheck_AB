

package factoryManager;

public class ProgramErrorException extends RuntimeException {

    private static final long serialVersionUID = 37473L;
    
    public ProgramErrorException(String message) {
        super(message);
    }
    
    public ProgramErrorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProgramErrorException(Throwable cause) {
        super(cause);
    }
}
