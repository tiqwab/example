/**
 * Check oop for instance of a simple class
 */
class Sample4 {
    static {
        System.loadLibrary("sample");
    }

    public static native int checkInstance(Foo foo);

    public static void main(String[] args) {
        Foo foo = new Foo(1, 0x1234567890123456L, "hello");
        System.out.println("foo.x: " + checkInstance(foo));
    }
}

class Foo {
    public int x;
    public long y;
    public String z;

    public Foo(int x, long y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
