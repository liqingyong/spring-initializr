FROM 172.22.96.1/library/circleci/jdk8:0.1.1
MAINTAINER 作者
ADD /#【name】.jar //
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/#【name】.jar"]
