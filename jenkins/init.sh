cd /opt/laokou && sed -i 's/\r$//' build.sh && chmod u+x  build.sh
cd /opt/laokou && chmod u+x laokou-admin.sh && sed -i 's/\r$//' laokou-admin.sh
cd /opt/laokou && chmod u+x laokou-gateway.sh && sed -i 's/\r$//' laokou-gateway.sh
cd /opt/laokou && chmod u+x laokou-register.sh && sed -i 's/\r$//' laokou-register.sh
cd /opt/laokou && chmod u+x laokou-monitor.sh && sed -i 's/\r$//' laokou-monitor.sh