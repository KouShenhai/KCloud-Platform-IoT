import cv2
import numpy as np
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# --- 1. 数据加载与预处理 ---
data_dir = 'data'
image_size = (128, 128) # 更大的图片尺寸可能需要更多内存和计算
batch_size = 32

# 使用ImageDataGenerator进行数据增强和加载
datagen = ImageDataGenerator(
    rescale=1./255, # 归一化像素值到0-1
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True,
    validation_split=0.2 # 20%用于验证集
)

train_generator = datagen.flow_from_directory(
    data_dir,
    target_size=image_size,
    batch_size=batch_size,
    class_mode='categorical', # 如果有两个类别，使用'categorical'
    subset='training'
)

validation_generator = datagen.flow_from_directory(
    data_dir,
    target_size=image_size,
    batch_size=batch_size,
    class_mode='categorical',
    subset='validation'
)

# 获取类别名和对应的索引
class_indices = train_generator.class_indices
print("类别索引:", class_indices)
num_classes = len(class_indices)

# --- 2. 构建CNN模型 ---
model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(image_size[0], image_size[1], 3)),
    MaxPooling2D((2, 2)),
    Conv2D(64, (3, 3), activation='relu'),
    MaxPooling2D((2, 2)),
    Conv2D(128, (3, 3), activation='relu'),
    MaxPooling2D((2, 2)),
    Flatten(),
    Dense(128, activation='relu'),
    Dropout(0.5), # 防止过拟合
    Dense(num_classes, activation='softmax') # 输出层，根据类别数量调整
])

model.compile(optimizer='adam',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

model.summary()

# --- 3. 训练模型 ---
epochs = 20 # 根据数据集大小和模型复杂度调整

history = model.fit(
    train_generator,
    steps_per_epoch=train_generator.samples // batch_size,
    validation_data=validation_generator,
    validation_steps=validation_generator.samples // batch_size,
    epochs=epochs
)

# --- 4. 预测函数 ---
def predict_image_status_cnn(image_path, model, class_indices, image_size=(128, 128)):
    img = cv2.imread(image_path)
    if img is None:
        return "无法加载图片"
    img = cv2.resize(img, image_size)
    img = img / 255.0 # 归一化
    img = np.expand_dims(img, axis=0) # 增加batch维度

    predictions = model.predict(img)
    predicted_class_index = np.argmax(predictions)

    # 反转字典，通过索引找到类别名
    idx_to_class = {v: k for k, v in class_indices.items()}
    predicted_class_name = idx_to_class[predicted_class_index]
    return predicted_class_name

# 假设你已经有训练好的模型和 class_indices
# 为了演示，我们先保存模型
model.save('model_status_classifier.h5')

# 再次加载模型进行预测（模拟实际部署）
from tensorflow.keras.models import load_model
loaded_model = load_model('model_status_classifier.h5')

# 假设你的类别索引是 {'offline': 0, 'online': 1}
# 在实际情况中，这个应该从 train_generator.class_indices 获取
mock_class_indices = {'offline': 0, 'online': 1}

# 使用上面的模拟图片进行测试
status_online_cnn = predict_image_status_cnn('test_online.png', loaded_model, mock_class_indices)
print(f"CNN预测图片 'test_online.png' 的模型状态是: {status_online_cnn}")

status_offline_cnn = predict_image_status_cnn('test_offline.png', loaded_model, mock_class_indices)
print(f"CNN预测图片 'test_offline.png' 的模型状态是: {status_offline_cnn}")
