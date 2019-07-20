#include <jni.h>

JNIEXPORT void JNICALL Java_Sample1_printJObject(JNIEnv *env, jobject obj) {
    printf("obj: %p\n", obj);
}
