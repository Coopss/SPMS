#!/bin/bash
sudo su <<EOF

rm -rf /var/www/html/* /opt/tomcat/latest/webapps/SPMS /opt/tomcat/latest/webapps/SPMS.war
sleep 5

EOF
