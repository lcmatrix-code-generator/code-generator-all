package top.lcmatrix.util.codegenerator.common.plugin;

public class ContextHolder {
    private static final ThreadLocal<Global> globalHolder = new ThreadLocal<>();
    private static final ThreadLocal<Object> inputHolder = new ThreadLocal<>();

    public static void setGlobal(Global global){
        globalHolder.set(global);
    }

    public static Global getGlobal(){
        return globalHolder.get();
    }

    public static void setInput(Object input){
        inputHolder.set(input);
    }

    public static Object getInput(){
        return inputHolder.get();
    }
}
