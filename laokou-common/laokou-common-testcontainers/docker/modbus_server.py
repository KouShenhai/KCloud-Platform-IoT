#!/usr/bin/env python3
"""
Modbus UDP Server Simulator using pymodbus.
Supports both TCP and UDP protocols based on command line arguments.

Usage:
    python modbus_server.py --protocol tcp --port 502
    python modbus_server.py --protocol udp --port 502
"""

import argparse
import logging
from pymodbus.datastore import ModbusSequentialDataBlock, ModbusSlaveContext, ModbusServerContext
from pymodbus.server import StartTcpServer, StartUdpServer

# Configure logging
logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)


def create_datastore():
    """
    Create a Modbus datastore with sample data.
    
    Modbus data types:
    - Coils (di): Read/Write discrete outputs (bits), address 0x0001-0xFFFF
    - Discrete Inputs (co): Read-only discrete inputs (bits), address 0x0001-0xFFFF  
    - Input Registers (ir): Read-only 16-bit registers, address 0x0001-0xFFFF
    - Holding Registers (hr): Read/Write 16-bit registers, address 0x0001-0xFFFF
    """
    # Initialize data blocks with sample values
    # 100 coils starting at address 0, all set to True (1)
    coils = ModbusSequentialDataBlock(0, [True] * 100)
    
    # 100 discrete inputs starting at address 0, alternating values
    discrete_inputs = ModbusSequentialDataBlock(0, [i % 2 == 0 for i in range(100)])
    
    # 100 input registers starting at address 0, values 0-99
    input_registers = ModbusSequentialDataBlock(0, list(range(100)))
    
    # 100 holding registers starting at address 0, values 100-199
    holding_registers = ModbusSequentialDataBlock(0, list(range(100, 200)))
    
    # Create slave context with all data blocks
    store = ModbusSlaveContext(
        di=discrete_inputs,  # Discrete Inputs
        co=coils,            # Coils
        hr=holding_registers, # Holding Registers
        ir=input_registers    # Input Registers
    )
    
    # Create server context (single slave, slave ID 1)
    context = ModbusServerContext(slaves=store, single=True)
    
    return context


def run_server(protocol: str, host: str, port: int):
    """
    Start the Modbus server with the specified protocol.
    
    Args:
        protocol: 'tcp' or 'udp'
        host: Host address to bind to
        port: Port number to listen on
    """
    context = create_datastore()
    
    log.info(f"Starting Modbus {protocol.upper()} server on {host}:{port}")
    log.info("Data blocks initialized:")
    log.info("  - Coils: 100 items (all True)")
    log.info("  - Discrete Inputs: 100 items (alternating)")
    log.info("  - Input Registers: 100 items (values 0-99)")
    log.info("  - Holding Registers: 100 items (values 100-199)")
    
    if protocol.lower() == 'tcp':
        StartTcpServer(
            context=context,
            address=(host, port)
        )
    elif protocol.lower() == 'udp':
        StartUdpServer(
            context=context,
            address=(host, port)
        )
    else:
        raise ValueError(f"Unknown protocol: {protocol}. Use 'tcp' or 'udp'.")


def main():
    parser = argparse.ArgumentParser(description='Modbus Server Simulator')
    parser.add_argument(
        '--protocol', '-p',
        type=str,
        default='tcp',
        choices=['tcp', 'udp'],
        help='Protocol to use: tcp or udp (default: tcp)'
    )
    parser.add_argument(
        '--host', '-H',
        type=str,
        default='0.0.0.0',
        help='Host address to bind to (default: 0.0.0.0)'
    )
    parser.add_argument(
        '--port', '-P',
        type=int,
        default=502,
        help='Port number to listen on (default: 502)'
    )
    
    args = parser.parse_args()
    
    try:
        run_server(args.protocol, args.host, args.port)
    except KeyboardInterrupt:
        log.info("Server stopped by user")
    except Exception as e:
        log.error(f"Server error: {e}")
        raise


if __name__ == '__main__':
    main()
