import ImgCrop from "antd-img-crop";
import {GetProp, message, Upload, UploadFile, UploadProps} from "antd";
import {getAccessToken} from "@/access";

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

interface UploadAvatarDrawerProps {
	setPreviewImage: (visible: string) => void;
	setPreviewOpen: (visible: boolean) => void;
	fileList: UploadFile[];
	setFileList: (visible: UploadFile[]) => void;
}

export const UploadAvatarDrawer: React.FC<UploadAvatarDrawerProps> = ({ setPreviewOpen, setPreviewImage, fileList, setFileList }) => {

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
		if (newFileList[0]?.response) {
			const res = newFileList[0]?.response
			if (res.code === 'OK') {
				message.success("上传成功").then()
				setFileList(newFileList);
			} else {
				message.error(res.msg).then()
				setFileList([])
			}
		} else {
			setFileList(newFileList)
		}
	}

	return (
		<ImgCrop rotationSlider>
			<Upload
				accept={'.jpg,.jpeg,.png,.gif,.webp'}
				action={`/api/admin/v3/oss/upload`}
				listType="picture-card"
				fileList={fileList}
				onChange={onChange}
				onPreview={handlePreview}
				headers={{ Authorization: `Bearer ${getAccessToken()}` }}
			>
				{fileList.length < 1 && '上传头像'}
			</Upload>
		</ImgCrop>
	);
};
