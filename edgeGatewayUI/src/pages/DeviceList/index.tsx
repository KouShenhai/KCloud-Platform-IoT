import { request } from '@umijs/max';
import { Button, message, Space, Table, Popconfirm, Modal, Form, Input, Select } from 'antd';
import React, { useEffect, useState } from 'react';

const DeviceList: React.FC = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form] = Form.useForm();

  const fetchDevices = async () => {
    setLoading(true);
    try {
      const res = await request('/api/v1/devices');
      if (res.code === 200) {
        setData(res.data || []);
      }
    } catch (e) {
      message.error('Failed to fetch devices');
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchDevices();
  }, []);

  const handleDelete = async (id: string) => {
    try {
      const res = await request(`/api/v1/devices/${id}`, { method: 'DELETE' });
      if (res.code === 200) {
        message.success('Deleted successfully');
        fetchDevices();
      }
    } catch (e) {
      message.error('Delete failed');
    }
  };

  const handleAdd = async () => {
    try {
      const values = await form.validateFields();
      const res = await request('/api/v1/devices', {
        method: 'POST',
        data: values,
      });
      if (res.code === 200) {
        message.success('Added successfully');
        setIsModalOpen(false);
        form.resetFields();
        fetchDevices();
      }
    } catch (e) {
      message.error('Failed to add device');
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id' },
    { title: 'Name', dataIndex: 'name' },
    { title: 'Type', dataIndex: 'type' },
    { title: 'Status', dataIndex: 'status' },
    {
      title: 'Action',
      render: (_: any, record: any) => (
        <Space>
          <Popconfirm title="Delete?" onConfirm={() => handleDelete(record.id)}>
            <Button danger type="link">Delete</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <div style={{ marginBottom: 16 }}>
        <Button type="primary" onClick={() => setIsModalOpen(true)}>Add Device</Button>
      </div>
      <Table rowKey="id" columns={columns} dataSource={data} loading={loading} />

      <Modal title="Add Device" open={isModalOpen} onOk={handleAdd} onCancel={() => setIsModalOpen(false)}>
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Device Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="type" label="Device Type" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="sensor">Sensor</Select.Option>
              <Select.Option value="camera">Camera</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default DeviceList;
