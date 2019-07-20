#include <jni.h>

JNIEXPORT void JNICALL Java_Sample3_checkClassObject(JNIEnv *env, jobject obj, jclass clazz) {
    (*env)->GetStaticMethodID(env, clazz, "main", "([Ljava/lang/String;)V");
}
