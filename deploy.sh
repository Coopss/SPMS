#!/bin/bash
sudo su <<EOF

mv SPMS.war /opt/tomcat/latest/webapps/
mv public_html/* /var/www/html/

EOF
