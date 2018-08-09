#include <QGuiApplication>
#include <QQmlApplicationEngine>

#include <QDebug>
#include <QAndroidJniObject>
#include <jni.h>

int fibonacci2(int n)
{
    return QAndroidJniObject::callStaticMethod<jint>
        ("com/LoginActivity" // java class name
        , "qqqE" // method name
        , "(I)I" // signature
        , n);
}

int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);

    QGuiApplication app(argc, argv);
    int st = fibonacci2(5);
    qDebug() << QString::number(st);

    QQmlApplicationEngine engine;
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    if (engine.rootObjects().isEmpty())
        return -1;

    return app.exec();
}
