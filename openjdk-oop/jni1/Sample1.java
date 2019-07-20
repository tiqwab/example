/**
 * Try JNI
 */
class Sample1 {
    static {
        System.loadLibrary("sample");
    }

    public static native void printJObject();

    public static void main(String[] args) {
        printJObject();
    }
}
