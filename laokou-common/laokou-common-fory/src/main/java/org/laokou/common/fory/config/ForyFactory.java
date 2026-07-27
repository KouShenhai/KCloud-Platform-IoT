/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.fory.config;

import lombok.Getter;
import org.apache.fory.ThreadSafeFory;
import org.apache.fory.config.CompatibleMode;
import org.apache.fory.config.ForyBuilder;
import org.apache.fory.config.Language;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Getter
public final class ForyFactory {

	public static final ForyFactory INSTANCE = new ForyFactory();

	public static final int C_1 = 1;

	public static final int C_2 = 2;

	public static final int C_3 = 3;

	public static final int C_4 = 4;

	public static final int C_5 = 5;

	public static final int C_6 = 6;

	public static final int C_7 = 7;

	public static final int C_8 = 8;

	public static final int C_9 = 9;

	public static final int C_10 = 10;

	public static final int C_11 = 11;

	public static final int C_12 = 12;

	public static final int C_13 = 13;

	public static final int C_14 = 14;

	public static final int C_15 = 15;

	public static final int C_16 = 16;

	public static final int C_17 = 17;

	public static final int C_18 = 18;

	public static final int C_19 = 19;

	public static final int C_20 = 20;

	public static final int C_21 = 21;

	public static final int C_22 = 22;

	public static final int C_23 = 23;

	public static final int C_24 = 24;

	public static final int C_25 = 25;

	public static final int C_26 = 26;

	public static final int C_27 = 27;

	public static final int C_28 = 28;

	public static final int C_29 = 29;

	public static final int C_30 = 30;

	public static final int C_31 = 31;

	public static final int C_32 = 32;

	public static final int C_33 = 33;

	public static final int C_34 = 34;

	public static final int C_35 = 35;

	public static final int C_36 = 36;

	public static final int C_37 = 37;

	public static final int C_38 = 38;

	public static final int C_39 = 39;

	public static final int C_40 = 40;

	public static final int C_41 = 41;

	public static final int C_42 = 42;

	public static final int C_43 = 43;

	public static final int C_44 = 44;

	public static final int C_45 = 45;

	public static final int C_46 = 46;

	public static final int C_47 = 47;

	public static final int C_48 = 48;

	public static final int C_49 = 49;

	public static final int C_50 = 50;

	public static final int C_51 = 51;

	public static final int C_52 = 52;

	public static final int C_53 = 53;

	public static final int C_54 = 54;

	public static final int C_55 = 55;

	public static final int C_56 = 56;

	public static final int C_57 = 57;

	public static final int C_58 = 58;

	public static final int C_59 = 59;

	public static final int C_60 = 60;

	public static final int C_61 = 61;

	public static final int C_62 = 62;

	public static final int C_63 = 63;

	public static final int C_64 = 64;

	public static final int C_65 = 65;

	public static final int C_66 = 66;

	public static final int C_67 = 67;

	public static final int C_68 = 68;

	public static final int C_69 = 69;

	public static final int C_70 = 70;

	public static final int C_71 = 71;

	public static final int C_72 = 72;

	public static final int C_73 = 73;

	public static final int C_74 = 74;

	public static final int C_75 = 75;

	public static final int C_76 = 76;

	public static final int C_77 = 77;

	public static final int C_78 = 78;

	public static final int C_79 = 79;

	public static final int C_80 = 80;

	public static final int C_81 = 81;

	public static final int C_82 = 82;

	public static final int C_83 = 83;

	public static final int C_84 = 84;

	public static final int C_85 = 85;

	public static final int C_86 = 86;

	public static final int C_87 = 87;

	public static final int C_88 = 88;

	public static final int C_89 = 89;

	public static final int C_90 = 90;

	public static final int C_91 = 91;

	public static final int C_92 = 92;

	public static final int C_93 = 93;

	public static final int C_94 = 94;

	public static final int C_95 = 95;

	public static final int C_96 = 96;

	public static final int C_97 = 97;

	public static final int C_98 = 98;

	public static final int C_99 = 99;

	public static final int C_100 = 100;

	public static final int C_101 = 101;

	public static final int C_102 = 102;

	public static final int C_103 = 103;

	public static final int C_104 = 104;

	public static final int C_105 = 105;

	public static final int C_106 = 106;

	public static final int C_107 = 107;

	public static final int C_108 = 108;

	public static final int C_109 = 109;

	public static final int C_110 = 110;

	public static final int C_111 = 111;

	public static final int C_112 = 112;

	public static final int C_113 = 113;

	public static final int C_114 = 114;

	public static final int C_115 = 115;

	public static final int C_116 = 116;

	public static final int C_117 = 117;

	public static final int C_118 = 118;

	public static final int C_119 = 119;

	public static final int C_120 = 120;

	public static final int C_121 = 121;

	public static final int C_122 = 122;

	public static final int C_123 = 123;

	public static final int C_124 = 124;

	public static final int C_125 = 125;

	public static final int C_126 = 126;

	public static final int C_127 = 127;

	public static final int C_128 = 128;

	public static final int C_129 = 129;

	public static final int C_130 = 130;

	public static final int C_131 = 131;

	public static final int C_132 = 132;

	public static final int C_133 = 133;

	public static final int C_134 = 134;

	public static final int C_135 = 135;

	public static final int C_136 = 136;

	public static final int C_137 = 137;

	public static final int C_138 = 138;

	public static final int C_139 = 139;

	public static final int C_140 = 140;

	public static final int C_141 = 141;

	public static final int C_142 = 142;

	public static final int C_143 = 143;

	public static final int C_144 = 144;

	public static final int C_145 = 145;

	public static final int C_146 = 146;

	public static final int C_147 = 147;

	public static final int C_148 = 148;

	public static final int C_149 = 149;

	public static final int C_150 = 150;

	public static final int C_151 = 151;

	public static final int C_152 = 152;

	public static final int C_153 = 153;

	public static final int C_154 = 154;

	public static final int C_155 = 155;

	public static final int C_156 = 156;

	public static final int C_157 = 157;

	public static final int C_158 = 158;

	public static final int C_159 = 159;

	public static final int C_160 = 160;

	public static final int C_161 = 161;

	public static final int C_162 = 162;

	public static final int C_163 = 163;

	public static final int C_164 = 164;

	public static final int C_165 = 165;

	public static final int C_166 = 166;

	public static final int C_167 = 167;

	public static final int C_168 = 168;

	public static final int C_169 = 169;

	public static final int C_170 = 170;

	public static final int C_171 = 171;

	public static final int C_172 = 172;

	public static final int C_173 = 173;

	public static final int C_174 = 174;

	public static final int C_175 = 175;

	public static final int C_176 = 176;

	public static final int C_177 = 177;

	public static final int C_178 = 178;

	public static final int C_179 = 179;

	public static final int C_180 = 180;

	public static final int C_181 = 181;

	public static final int C_182 = 182;

	public static final int C_183 = 183;

	public static final int C_184 = 184;

	public static final int C_185 = 185;

	public static final int C_186 = 186;

	public static final int C_187 = 187;

	public static final int C_188 = 188;

	public static final int C_189 = 189;

	public static final int C_190 = 190;

	public static final int C_191 = 191;

	public static final int C_192 = 192;

	public static final int C_193 = 193;

	public static final int C_194 = 194;

	public static final int C_195 = 195;

	public static final int C_196 = 196;

	public static final int C_197 = 197;

	public static final int C_198 = 198;

	public static final int C_199 = 199;

	public static final int C_200 = 200;

	public static final int C_201 = 201;

	public static final int C_202 = 202;

	public static final int C_203 = 203;

	public static final int C_204 = 204;

	public static final int C_205 = 205;

	public static final int C_206 = 206;

	public static final int C_207 = 207;

	public static final int C_208 = 208;

	public static final int C_209 = 209;

	public static final int C_210 = 210;

	public static final int C_211 = 211;

	public static final int C_212 = 212;

	public static final int C_213 = 213;

	public static final int C_214 = 214;

	public static final int C_215 = 215;

	public static final int C_216 = 216;

	public static final int C_217 = 217;

	public static final int C_218 = 218;

	public static final int C_219 = 219;

	public static final int C_220 = 220;

	public static final int C_221 = 221;

	public static final int C_222 = 222;

	public static final int C_223 = 223;

	public static final int C_224 = 224;

	public static final int C_225 = 225;

	public static final int C_226 = 226;

	public static final int C_227 = 227;

	public static final int C_228 = 228;

	public static final int C_229 = 229;

	public static final int C_230 = 230;

	public static final int C_231 = 231;

	public static final int C_232 = 232;

	public static final int C_233 = 233;

	public static final int C_234 = 234;

	public static final int C_235 = 235;

	public static final int C_236 = 236;

	public static final int C_237 = 237;

	public static final int C_238 = 238;

	public static final int C_239 = 239;

	public static final int C_240 = 240;

	public static final int C_241 = 241;

	public static final int C_242 = 242;

	public static final int C_243 = 243;

	public static final int C_244 = 244;

	public static final int C_245 = 245;

	public static final int C_246 = 246;

	public static final int C_247 = 247;

	public static final int C_248 = 248;

	private final ThreadSafeFory fory = new ForyBuilder()
		// 关闭多语言序列化
		.withXlang(false)
		// 启用JAVA序列化
		.withLanguage(Language.JAVA)
		// 启用循环引用引用跟踪.
		.withRefTracking(true)
		// 压缩整数以节省空间
		.withIntCompressed(true)
		// 压缩长整数以节省空间
		.withLongCompressed(true)
		// CompatibleMode.SCHEMA_CONSISTENT模式序列化对象
		.withCompatibleMode(CompatibleMode.SCHEMA_CONSISTENT)
		// 启用异步多线程编译
		.withAsyncCompilation(true)
		// 启用类注册
		.requireClassRegistration(true)
		// 支持反序列化不存在或未知的类
		.withDeserializeUnknownClass(true)
		// 限制嵌套反序列化深度
		.withMaxDepth(100)
		.buildThreadSafeFory();

	public <T> void register(Class<T> clazz, int num) {
		fory.register(clazz, num);
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return new byte[0];
		}
		if (object instanceof String str) {
			return str.getBytes(StandardCharsets.UTF_8);
		}
		return fory.serialize(object);
	}

	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return fory.deserialize(bytes);
	}

}
