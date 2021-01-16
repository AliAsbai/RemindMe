FROM tomcat:8.0-jre8
ADD /ServerUser-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY /server.xml /usr/local/tomcat/conf/server.xml
COPY ./ssl /usr/local/tomcat/ssl
CMD ["catalina.sh", "run"]