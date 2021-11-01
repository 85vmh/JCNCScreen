#include <hal.h>
#include <stdio.h>
#include <com_mindovercnc_linuxcnc_HalHandler.h>


union paramunion {
    hal_bit_t b;
    hal_u32_t u32;
    hal_s32_t s32;
    hal_float_t f;
};

union pinunion {
    void *v;
    hal_bit_t *b;
    hal_u32_t *u32;
    hal_s32_t *s32;
    hal_float_t *f;
};

union halunion {
    union pinunion pin;
    union paramunion param;
};

union haldirunion {
    hal_pin_dir_t pindir;
    hal_param_dir_t paramdir;
};

struct halitem {
    bool is_pin;
    hal_type_t type;
    union haldirunion dir;
    union halunion *u;
};

struct pyhalitem {
    halitem  pin;
    char * name;
};


/*
 * Class:     com_mindovercnc_linuxcnc_HalHandler
 * Method:    createComponent
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_mindovercnc_linuxcnc_HalHandler_createComponent
  (JNIEnv *env, jobject thisObject, jstring componentName){
    const char * compName = env->GetStringUTFChars(componentName, NULL);
    int result =  hal_init(compName);
    //release
    return result
  }

/*
 * Class:     com_mindovercnc_linuxcnc_HalHandler
 * Method:    addPin
 * Signature: (Lcom/mindovercnc/linuxcnc/HalHandler/Pin;I)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_HalHandler_addPin
  (JNIEnv *env, jobject thisObject, jobject pinObject, jint componentId){


    char pin_name[HAL_NAME_LEN+1];
    int res;
    halitem pin;
    pin.is_pin = 1;

    if(type < HAL_BIT || type > HAL_U32) {
        //PyErr_Format(pyhal_error_type, "Invalid pin type %d", type);
        return NULL;
    }

    pin.type = type;
    pin.dir.pindir = dir;
    pin.u = (halunion*)hal_malloc(sizeof(halunion));
    if(!pin.u) {
        //PyErr_SetString(PyExc_MemoryError, "hal_malloc failed");
        return NULL;
    }

    res = snprintf(pin_name, sizeof(pin_name), "%s.%s", self->prefix, name);
    if(res > HAL_NAME_LEN || res < 0) {
//        PyErr_Format(pyhal_error_type,
//            "Invalid pin name length \"%s.%s\": max = %d characters",
//            self->prefix, name, HAL_NAME_LEN);
        return NULL;
    }
    res = hal_pin_new(pin_name, type, dir, (void**)pin.u, self->hal_id);
    if(res) return pyhal_error(res);

    (*self->items)[name] = pin;




    printf("addPin called");
  }

/*
 * Class:     com_mindovercnc_linuxcnc_HalHandler
 * Method:    getPin
 * Signature: (Ljava/lang/String;I)Lcom/mindovercnc/linuxcnc/HalHandler/Pin;
 */
JNIEXPORT jobject JNICALL Java_com_mindovercnc_linuxcnc_HalHandler_getPin
  (JNIEnv *env, jobject thisObject, jstring pinName, jint componentId){
    printf("getPin called");
    return NULL;
  }

/*
 * Class:     com_mindovercnc_linuxcnc_HalHandler
 * Method:    setReady
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_mindovercnc_linuxcnc_HalHandler_setReady
  (JNIEnv *env, jobject thisObject, jint componentId){
    printf("setReady called");
  }
