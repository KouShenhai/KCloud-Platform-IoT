# /*
#  * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
#  *
#  * Licensed under the Apache License, Version 2.0 (the "License");
#  * you may not use this file except in compliance with the License.
#  * You may obtain a copy of the License at
#  *
#  *   http://www.apache.org/licenses/LICENSE-2.0
#  *
#  * Unless required by applicable law or agreed to in writing, software
#  * distributed under the License is distributed on an "AS IS" BASIS,
#  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  * See the License for the specific language governing permissions and
#  * limitations under the License.
#  *
#  */

"""
神经网络模型
"""

import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers


def create_wake_word_model(input_shape: tuple = None,
                           num_classes: int = 2) -> keras.Model:
    """
    创建唤醒词检测模型

    轻量级CNN架构，适用于边缘设备部署
    模型大小约 50-100KB

    Args:
        input_shape: 输入形状 (num_frames, n_mfcc, 1)
        num_classes: 分类数量

    Returns:
        Keras模型
    """
    if input_shape is None:
        input_shape = (201, 40, 1)

    model = keras.Sequential([
        # 输入层
        layers.InputLayer(input_shape=input_shape),

        # 第一个卷积块
        layers.Conv2D(16, (3, 3), padding='same'),
        layers.BatchNormalization(),
        layers.ReLU(),
        layers.MaxPooling2D((2, 2)),
        layers.Dropout(0.2),

        # 第二个卷积块
        layers.Conv2D(32, (3, 3), padding='same'),
        layers.BatchNormalization(),
        layers.ReLU(),
        layers.MaxPooling2D((2, 2)),
        layers.Dropout(0.2),

        # 第三个卷积块
        layers.Conv2D(64, (3, 3), padding='same'),
        layers.BatchNormalization(),
        layers.ReLU(),
        layers.MaxPooling2D((2, 2)),
        layers.Dropout(0.3),

        # 全连接层
        layers.Flatten(),
        layers.Dense(64),
        layers.ReLU(),
        layers.Dropout(0.3),

        # 输出层
        layers.Dense(num_classes, activation='softmax')
    ], name='wake_word_model')

    return model


def create_tiny_model(input_shape: tuple = None,
                      num_classes: int = 2) -> keras.Model:
    """
    创建超轻量级模型 (约20KB)

    适用于资源极度受限的设备

    Args:
        input_shape: 输入形状
        num_classes: 分类数量

    Returns:
        Keras模型
    """
    if input_shape is None:
        input_shape = (201, 40, 1)

    model = keras.Sequential([
        layers.InputLayer(input_shape=input_shape),

        # 深度可分离卷积 (更轻量)
        layers.DepthwiseConv2D((3, 3), padding='same'),
        layers.BatchNormalization(),
        layers.ReLU(),
        layers.Conv2D(8, (1, 1)),
        layers.MaxPooling2D((2, 2)),

        layers.DepthwiseConv2D((3, 3), padding='same'),
        layers.BatchNormalization(),
        layers.ReLU(),
        layers.Conv2D(16, (1, 1)),
        layers.MaxPooling2D((2, 2)),

        # 全局平均池化
        layers.GlobalAveragePooling2D(),

        layers.Dense(32, activation='relu'),
        layers.Dense(num_classes, activation='softmax')
    ], name='tiny_wake_word_model')

    return model


def compile_model(model: keras.Model,
                  learning_rate: float = 0.001) -> keras.Model:
    """
    编译模型

    Args:
        model: Keras模型
        learning_rate: 学习率

    Returns:
        编译后的模型
    """
    model.compile(
        optimizer=keras.optimizers.Adam(learning_rate=learning_rate),
        loss='sparse_categorical_crossentropy',
        metrics=['accuracy']
    )
    return model


def get_model_summary(model: keras.Model) -> str:
    """
    获取模型摘要

    Args:
        model: Keras模型

    Returns:
        模型摘要字符串
    """
    string_list = []
    model.summary(print_fn=lambda x: string_list.append(x))
    return "\n".join(string_list)


def count_parameters(model: keras.Model) -> dict:
    """
    统计模型参数

    Args:
        model: Keras模型

    Returns:
        参数统计字典
    """
    trainable = sum([tf.size(w).numpy() for w in model.trainable_weights])
    non_trainable = sum([tf.size(w).numpy() for w in model.non_trainable_weights])

    return {
        "trainable": trainable,
        "non_trainable": non_trainable,
        "total": trainable + non_trainable
    }
