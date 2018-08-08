#include <jni.h>
#include <QCoreApplication>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL
  Java_poc_intent_FingerprintHandler_setUrl(JNIEnv *env,
                                        jobject obj,
                                        jstring url)
{
    (void)obj;
    const char *urlStr = env->GetStringUTFChars(url, NULL);

    QCoreApplication::postEvent(QCoreApplication::instance(), new QFileOpenEvent(QUrl(QString(urlStr))));

    env->ReleaseStringUTFChars(url, urlStr);
    return;
}

#ifdef __cplusplus
}
#endif
