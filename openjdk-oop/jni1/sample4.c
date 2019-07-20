#include <jni.h>

JNIEXPORT jint JNICALL Java_Sample4_checkInstance(JNIEnv *env, jobject obj, jobject foo) {
    jclass clazz = (*env)->GetObjectClass(env, foo);
    jfieldID id = (*env)->GetFieldID(env, clazz, "x", "I");
    return (*env)->GetIntField(env, foo, id);
}
