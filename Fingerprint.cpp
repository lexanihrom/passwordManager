#include "Fingerprint.hpp"

#include <QAndroidJniObject>
#include <QDebug>
#include <QtAndroid>
#include <QUrl>
#include <jni.h>

Fingerprint *Fingerprint::m_instance = nullptr;

Fingerprint::Fingerprint(QObject *parent)
    : QObject(parent)
{
    m_instance = this;
    m_pendingIntentChecked = false;
}

Fingerprint *Fingerprint::instance()
{
    if(!m_instance)
        m_instance = new Fingerprint;

    return m_instance;
}

void Fingerprint::appStateChanged(Qt::ApplicationState state)
{
    if (state == Qt::ApplicationActive) {
        if(!m_pendingIntentChecked) {
            m_pendingIntentChecked = true;
            Fingerprint::instance()->checkPendingIntent();
        }
    }
}

void Fingerprint::checkPendingIntent()
{
    QAndroidJniObject activity = QtAndroid::androidActivity();

    if(activity.isValid())
        activity.callMethod<void>("checkPendingIntent");
}

