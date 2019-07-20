#include <jni.h>

JNIEXPORT void JNICALL Java_Sample2_callArrayLength(JNIEnv *env, jobject obj, jarray arr) {
    (*env)->GetArrayLength(env, arr);
}
