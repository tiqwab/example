/**
 * Check oop for an array
 */
class Sample2 {
    static {
        System.loadLibrary("sample");
    }

    public static native void callArrayLength(int[] args);

    public static void main(String[] args) {
        int arr[] = {1, 2, 3};
        callArrayLength(arr);
    }
}
