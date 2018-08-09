#ifndef FINGERPRINT_HPP
#define FINGERPRINT_HPP

#include <QObject>

class Fingerprint : public QObject
{
    Q_OBJECT
public:
    Fingerprint(QObject* parent = 0);
    static Fingerprint *instance();

public slots:
    void appStateChanged(Qt::ApplicationState state);

signals:
    void incomingURL(const QUrl &path);

private:
    void checkPendingIntent();

    static Fingerprint *m_instance;
    bool m_pendingIntentChecked;
};

#endif // FINGERPRINT_HPP
