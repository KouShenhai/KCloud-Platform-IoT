import React, { useEffect, useRef, useState } from 'react';
import { Modal, Row, Col, Button, Space, Upload, message } from 'antd';
import { useIntl } from '@umijs/max';
import { uploadAvatar } from '@/services/system/user';
import { Cropper } from 'react-cropper';
import './cropper.css';
import styles from './index.less';
import {
  MinusOutlined,
  PlusOutlined,
  RedoOutlined,
  UndoOutlined,
  UploadOutlined,
} from '@ant-design/icons';

export type AvatarCropperProps = {
  onFinished: (isSuccess: boolean) => void;
  open: boolean;
  data: any;
};

const AvatarCropperForm: React.FC<AvatarCropperProps> = (props) => {
  const cropperRef = useRef<HTMLImageElement>(null);
  const [avatarData, setAvatarData] = useState<any>();
  const [previewData, setPreviewData] = useState();

  useEffect(() => {
    setAvatarData(props.data);
  }, [props]);

  const intl = useIntl();
  const handleOk = () => {
    const imageElement: any = cropperRef?.current;
    const cropper: any = imageElement?.cropper;
    cropper.getCroppedCanvas().toBlob((blob: Blob) => {
      const formData = new FormData();
      formData.append('avatarfile', blob);
      uploadAvatar(formData).then((res) => {
        if (res.code === 200) {
          message.success(res.msg);
          props.onFinished(true);
        } else {
          message.warning(res.msg);
        }
      });
    }, 'image/png');
  };
  const handleCancel = () => {
    props.onFinished(false);
  };
  const onCrop = () => {
    const imageElement: any = cropperRef?.current;
    const cropper: any = imageElement?.cropper;
    setPreviewData(cropper.getCroppedCanvas().toDataURL());
  };
  const onRotateRight = () => {
    const imageElement: any = cropperRef?.current;
    const cropper: any = imageElement?.cropper;
    cropper.rotate(90);
  };
  const onRotateLeft = () => {
    const imageElement: any = cropperRef?.current;
    const cropper: any = imageElement?.cropper;
    cropper.rotate(-90);
  };
  const onZoomIn = () => {
    const imageElement: any = cropperRef?.current;
    const cropper: any = imageElement?.cropper;
    cropper.zoom(0.1);
  };
  const onZoomOut = () => {
    const imageElement: any = cropperRef?.current;
    const cropper: any = imageElement?.cropper;
    cropper.zoom(-0.1);
  };
  const beforeUpload = (file: any) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      setAvatarData(reader.result);
    };
  };
  return (
    <Modal
      width={800}
      title={intl.formatMessage({
        id: 'system.user.modify_avatar',
        defaultMessage: '修改头像',
      })}
      open={props.open}
      destroyOnClose
      onOk={handleOk}
      onCancel={handleCancel}
    >
      <Row gutter={[16, 16]}>
        <Col span={12} order={1}>
          <Cropper
            ref={cropperRef}
            src={avatarData}
            style={{ height: 350, width: '100%', marginBottom: '16px' }}
            initialAspectRatio={1}
            guides={false}
            crop={onCrop}
            zoomable={true}
            zoomOnWheel={true}
            rotatable={true}
          />
        </Col>
        <Col span={12} order={2}>
          <div className={styles.avatarPreview}>
            <img src={previewData} style={{ height: '100%', width: '100%' }} />
          </div>
        </Col>
      </Row>
      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Upload beforeUpload={beforeUpload} maxCount={1}>
            <Button>
              <UploadOutlined />
              上传
            </Button>
          </Upload>
        </Col>
        <Col>
          <Space>
            <Button icon={<RedoOutlined />} onClick={onRotateRight} />
            <Button icon={<UndoOutlined />} onClick={onRotateLeft} />
            <Button icon={<PlusOutlined />} onClick={onZoomIn} />
            <Button icon={<MinusOutlined />} onClick={onZoomOut} />
          </Space>
        </Col>
      </Row>
    </Modal>
  );
};

export default AvatarCropperForm;
