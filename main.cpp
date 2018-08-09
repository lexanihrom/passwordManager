#include <QGuiApplication>
#include <QQmlApplicationEngine>

#include <QDebug>
#include <QAndroidJniObject>
#include <jni.h>

int fibonacci(int n)
{
//    return QAndroidJniObject::callStaticMethod<jint>
//        ("com/Fingerprint" // java class name
//        , "fibonacci" // method name
//        , "(I)I" // signature
//        , n);
    return n;
}

int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);

    QGuiApplication app(argc, argv);
    //int st = fibonacci(8);
    //qDebug() << QString::number(st);

    QQmlApplicationEngine engine;
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    if (engine.rootObjects().isEmpty())
        return -1;

    return app.exec();
}
