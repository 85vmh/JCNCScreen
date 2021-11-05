#include <hal.h>
#include <stdio.h>
#include <map>
#include <string>
#include <com_mindovercnc_linuxcnc_HalHandler.h>
#include <com_mindovercnc_linuxcnc_HalComponent.h>


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

typedef std::map<std::string, struct halitem> itemmap;

typedef struct halobject {
    int hal_id;
    char *name;
    char *prefix;
    itemmap *items;
} halobject;


/*
 * Class:     com_mindovercnc_linuxcnc_HalHandler
 * Method:    createComponent
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jobject JNICALL Java_com_mindovercnc_linuxcnc_HalHandler_createComponent
  (JNIEnv *env, jobject thisObject, jstring componentName){
    const char * compName = env->GetStringUTFChars(componentName, NULL);
    int result =  hal_init(compName);
    env->ReleaseStringUTFChars(componentName, compName);

    if(result > 0){
        jclass clHalComp  = env->FindClass("com/mindovercnc/base/data/HalComponent");
        jfieldID fName    = env->GetFieldID(clHalComp, "name", "Ljava/lang/String;");
        jfieldID fCompId  = env->GetFieldID(clHalComp, "componentId", "I");

        jobject newHalComp = env->AllocObject(clHalComp);
        env->SetObjectField(newHalComp, fName, componentName);
        env->SetIntField(newHalComp, fCompId, result);

        return newHalComp;
    }
    return NULL;
  }

/*
 * Class:     com_mindovercnc_linuxcnc_HalComponent
 * Method:    addPin
 * Signature: (Lcom/mindovercnc/base/data/HalPin;)I
 */
JNIEXPORT jint JNICALL Java_com_mindovercnc_base_data_HalComponent_addPin
  (JNIEnv *env, jobject thisObject, jobject halPin){

    jclass clHalComp  = env->FindClass("com/mindovercnc/base/data/HalComponent");
    jfieldID fName    = env->GetFieldID(clHalComp, "name", "Ljava/lang/String;");
    jfieldID intCompIdField = env->GetFieldID(clHalComp, "componentId", "I");
    int componentId = env->GetIntField(thisObject, intCompIdField);
    jobject compNameObject = env->GetObjectField(thisObject, fName);
    jstring compName = (jstring)compNameObject;
    const char* compNameChars = env -> GetStringUTFChars(compName, 0);

    jclass clHalPin  = env->FindClass("com/mindovercnc/base/data/HalPin");
    jclass clPinType  = env->FindClass("com/mindovercnc/base/data/HalPin$Type");
    jclass clPinDir  = env->FindClass("com/mindovercnc/base/data/HalPin$Dir");

    jfieldID pinNameField  = env->GetFieldID(clHalPin, "name", "Ljava/lang/String;");
    jobject pinNameObj   = env->GetObjectField(halPin, pinNameField);
    jstring pinName = (jstring)pinNameObj;
    const char* pinNameChars = env -> GetStringUTFChars(pinName, 0);

    rtapi_print_msg(RTAPI_MSG_ERR, "pinName is: (%s)\n", pinNameChars);


    jfieldID pinTypeField = env->GetFieldID(clHalPin, "type", "Lcom/mindovercnc/base/data/HalPin$Type;");
    jobject pinTypeObj  = env->GetObjectField(halPin, pinTypeField);

    jmethodID pinTypeValueMethod = env->GetMethodID(clPinType, "getValue", "()I");
    jint typeValue = env->CallIntMethod(pinTypeObj, pinTypeValueMethod);

    rtapi_print_msg(RTAPI_MSG_ERR, "type is: (%i)\n", typeValue);
    
    
    
    jfieldID pinDirField = env->GetFieldID(clHalPin, "dir", "Lcom/mindovercnc/base/data/HalPin$Dir;");
    jobject pinDirObj  = env->GetObjectField(halPin, pinDirField);

    jmethodID pinDirValueMethod = env->GetMethodID(clPinDir, "getValue", "()I");
    jint dirValue = env->CallIntMethod(pinDirObj, pinDirValueMethod);

    rtapi_print_msg(RTAPI_MSG_ERR, "dir is: (%i)\n", dirValue);
    
    
    char pin_name[HAL_NAME_LEN+1];
    int result;

    halitem pin;
    pin.is_pin = 1;
    pin.type = (hal_type_t)typeValue;
    pin.dir.pindir = (hal_pin_dir_t)dirValue;
    pin.u = (halunion*)hal_malloc(sizeof(halunion));
    result = snprintf(pin_name, sizeof(pin_name), "%s.%s", compNameChars, pinNameChars);
    if(result > HAL_NAME_LEN || result < 0) {
            rtapi_print_msg(RTAPI_MSG_ERR,
                "Invalid pin name length \"%s.%s\": max = %d characters",
                compNameChars, pinNameChars, HAL_NAME_LEN);
    }

    result = hal_pin_new(pin_name, pin.type, pin.dir.pindir, (void**)pin.u, componentId);
    if(result) return result;

    rtapi_print_msg(RTAPI_MSG_ERR, "pinName with prefix is: (%s)\n", pin_name);

    return result;
  }

/*
 * Class:     com_mindovercnc_linuxcnc_HalComponent
 * Method:    getPin
 * Signature: (Ljava/lang/String;)Lcom/mindovercnc/base/data/HalPin;
 */
JNIEXPORT jobject JNICALL Java_com_mindovercnc_base_data_HalComponent_getPin
  (JNIEnv *env, jobject thisObject, jstring pinName){
    return NULL;
  }
