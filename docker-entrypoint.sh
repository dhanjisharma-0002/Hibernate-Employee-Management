#!/bin/sh
set -e

# Vercel container runtime routes to the port specified in $PORT (defaults to 80).
# Fallback to port 8080 for standard local container testing.
HTTP_PORT="${PORT:-80}"

echo "[docker-entrypoint] Configuring Apache Tomcat 10.1 connector to listen on port: ${HTTP_PORT}"

# Dynamically patch Tomcat conf/server.xml to listen on $HTTP_PORT
if [ -f "/usr/local/tomcat/conf/server.xml" ]; then
    sed -i "s/port=\"8080\"/port=\"${HTTP_PORT}\"/g" /usr/local/tomcat/conf/server.xml
fi

# Execute Tomcat in foreground
exec catalina.sh run
