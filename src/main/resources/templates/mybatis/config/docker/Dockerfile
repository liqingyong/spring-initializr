FROM 172.22.96.22:5000/8u131-jre-alpine:latest
MAINTAINER 作者
ADD /#【name】.jar //
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8080
ENTRYPOINT ["java","-jar","/#【name】.jar"]
