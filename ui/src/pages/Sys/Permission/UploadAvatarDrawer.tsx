import ImgCrop from "antd-img-crop";
import {GetProp, Upload, UploadFile, UploadProps} from "antd";
import {uploadUserAvatar} from "@/services/admin/user";
import React from "react";

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

interface UploadAvatarDrawerProps {
	setPreviewImage: (visible: string) => void;
	setPreviewOpen: (visible: boolean) => void;
	fileList: UploadFile[];
	setFileList: (visible: UploadFile[]) => void;
	setLogId: (logId: number) => void;
}

export const UploadAvatarDrawer: React.FC<UploadAvatarDrawerProps> = ({ setPreviewOpen, setPreviewImage, fileList, setFileList, setLogId }) => {

	const getBase64 = (file: FileType): Promise<string> =>
		new Promise((resolve, reject) => {
			const reader = new FileReader();
			reader.readAsDataURL(file);
			reader.onload = () => resolve(reader.result as string);
			reader.onerror = (error) => reject(error);
		});

	const handlePreview = async (file: UploadFile) => {
		if (!file.url && !file.preview) {
			file.preview = await getBase64(file.originFileObj as FileType);
		}
		setPreviewImage(file.url || (file.preview as string));
		setPreviewOpen(true);
	}

	const onChange: UploadProps['onChange'] = ({ fileList: newFileList }) => {
		if (newFileList[0]?.response && newFileList[0]?.response.code !== 'OK') {
			setFileList([])
		} else {
			setFileList(newFileList)
		}
	}

	return (
		<ImgCrop rotationSlider>
			<Upload
				accept={'.jpg,.jpeg,.png,.gif,.webp'}
				listType="picture-card"
				fileList={fileList}
				onChange={onChange}
				onPreview={handlePreview}
				customRequest={async ( options ) => {
					const { file, onProgress, onError, onSuccess } = options;
					const formData = new FormData();
					formData.append('file', file);
					uploadUserAvatar(formData).then(res => {
						// @ts-ignore
						onProgress({ percent: 100 }, file)
						if (res.code === 'OK') {
							// @ts-ignore
							onSuccess(res, file)
							// 修改日志ID
							setLogId(res.data.logId)
						} else {
							// @ts-ignore
							onError({status: 500, url: '', method: 'POST'}, res)
						}
					}).catch(() => {
						setFileList([])
					})
				}
			}
			>
				{fileList.length < 1 && '上传头像'}
			</Upload>
		</ImgCrop>
	);
};
