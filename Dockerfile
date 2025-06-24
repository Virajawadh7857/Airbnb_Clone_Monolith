FROM centos:7

MAINTAINER virajawadh01@gmail.com

RUN yum install -y httpd unzip

WORKDIR /var/www/html/

COPY ./build/ /var/www/html/

EXPOSE 80 443

CMD ["/usr/sbin/httpd", "-D", "FOREGROUND"]


