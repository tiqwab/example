/**
 * Check oop for java.lang.Class object
 */
class Sample3 {
    static {
        System.loadLibrary("sample");
    }

    public static native void checkClassObject(Class<?> clazz);

    public static void main(String[] args) {
        checkClassObject(Sample3.class);
    }
}
