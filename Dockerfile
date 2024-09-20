FROM tomcat:11.0-jdk21
COPY ./target/*.war /usr/local/tomcat/webapps/ROOT.war
ENV JPDA_ADDRESS="*:9090"
ENV JPDA_TRANSPORT="dt_socket"
EXPOSE 9090 9090
CMD ["catalina.sh","jpda","run"]