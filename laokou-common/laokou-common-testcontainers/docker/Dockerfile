# Modbus TCP/UDP Server Simulator
# Based on pymodbus library
# Supports both TCP and UDP protocols

FROM python:3.11-slim

LABEL maintainer="laokou"
LABEL description="Modbus TCP/UDP Server Simulator using pymodbus"
LABEL version="1.0"

# Set working directory
WORKDIR /app

# Install pymodbus
RUN pip install --no-cache-dir pymodbus==3.6.4

# Copy the server script
COPY modbus_server.py /app/

# Expose Modbus port (502 is standard, 5020 is common alternative)
EXPOSE 502
EXPOSE 5020

# Set environment variables
ENV MODBUS_PROTOCOL=tcp
ENV MODBUS_HOST=0.0.0.0
ENV MODBUS_PORT=502

# Default command - can be overridden with docker run arguments
# Examples:
#   TCP: docker run -p 502:502 modbus-sim --protocol tcp --port 502
#   UDP: docker run -p 502:502/udp modbus-sim --protocol udp --port 502
ENTRYPOINT ["python", "modbus_server.py"]
CMD ["--protocol", "tcp", "--host", "0.0.0.0", "--port", "502"]
