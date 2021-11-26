/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_mindovercnc_linuxcnc_CommandWriter */

#ifndef _Included_com_mindovercnc_linuxcnc_CommandWriter
#define _Included_com_mindovercnc_linuxcnc_CommandWriter
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_init
  (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_homeAxis
  (JNIEnv *, jobject, jint);

JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_unHomeAxis
  (JNIEnv *, jobject, jint);

JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_overrideLimits
  (JNIEnv *, jobject, jint);

JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_jogContinuous
  (JNIEnv *, jobject, jint, jint, jdouble);

JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_jogIncremental
  (JNIEnv *, jobject, jint, jint, jdouble, jdouble);


JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_jogAbsolute
  (JNIEnv *, jobject, jint, jint, jdouble, jdouble);


JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_jogStop
  (JNIEnv *, jobject, jint, jint);


JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setBacklash
  (JNIEnv*, jobject, jint, jdouble);

JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setMinPositionLimit
  (JNIEnv*, jobject, jint, jdouble);


JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setMaxPositionLimit
  (JNIEnv*, jobject, jint, jdouble);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    loadTaskPlan
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_loadTaskPlan
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    loadToolTable
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_loadToolTable
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    sendMDICommand
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_sendMDICommand
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setAuto
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setAuto
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setBlockDelete
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setBlockDelete
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setFeedOverride
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setFeedOverride
  (JNIEnv *, jobject, jdouble);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setFlood
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setFlood
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setMist
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setMist
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setOptionalStop
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setOptionalStop
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setRapidOverride
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setRapidOverride
  (JNIEnv *, jobject, jdouble);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setSpindle
 * Signature: (ZII)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setSpindle
  (JNIEnv *, jobject, jboolean, jint, jint);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setSpindleOverride
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setSpindleOverride
  (JNIEnv *, jobject, jdouble);


JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setFeedHold(JNIEnv*, jobject, jint);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setTaskMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setTaskMode
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    setTaskState
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_setTaskState
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_mindovercnc_linuxcnc_CommandWriter
 * Method:    taskAbort
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_CommandWriter_taskAbort
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
