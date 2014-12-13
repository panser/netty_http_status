netty_http_status
===========

Задача
===========
Необходимо реализовать http-сервер на фреймворке netty(http://netty.io/), со следующим функционалом:

1. По запросу на http://somedomain/hello отдает «Hello World» через 10 секунд
2. По запросу на http://somedomain/redirect?url=<url> происходит переадресация на указанный url
3. По запросу на http://somedomain/status выдается статистика:
 - общее количество запросов
 - количество уникальных запросов (по одному на IP)
 - счетчик запросов на каждый IP в виде таблицы с колонкам и IP, кол-во запросов, время последнего запроса
 - количество переадресаций по url'ам  в виде таблицы, с колонками url, кол-во переадресация
 - количество соединений, открытых в данный момент
 - в виде таблицы лог из 16 последних обработанных соединений, колонки src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)

Все это (вместе с особенностями имплементации в текстовом виде) выложить на github, приложить к этому:
 - скриншоты как выглядят станицы /status в рабочем приложении
 - скриншот результата выполнения команды ab – c 100 – n 10000 http://somedomain/status
 - еще один скриншот станицы /status, но уже после выполнение команды ab из предыдущего пункта

Комментарии:
 - использовать самую последнюю стабильную версию netty
 - обратить внимание на многопоточность
 - разобраться в EventLoop’ами netty
 - приложение должно собираться Maven'ом
 - все файлы должны быть в UTF8, перенос строки \n

Инструкция
===========

Для установки приложения необходимы:

1. Java 1.7
3. Maven

Для запуска сервера выполнить mvn compile assembly:single, приложение в консоли выведет адресс по которому с ним можно соединиться

Подробности
===========

Запись данных о запросах происходит с помощью классов из пакета ua.org.gostroy.netty_http_status.server.handler.statistic
Статистика пишиться в БД, используеться h2 через JPA.
URL парсяться в FrontController, для каждого вида URL - свой контроллер, так же реализован DefaultController для отдачи статики(bootstrap.css).
Темплейт страницы /status реализован на Velocity.