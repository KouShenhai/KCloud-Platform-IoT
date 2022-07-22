package io.laokou.common.utils;
import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * @author Kou Shenhai
 */
@Slf4j
public class FileUtil extends FileUtils {

    public static final String RW = "rw";

    // 定义允许上传的文件扩展名
    private static final Map<String, String> extMap = new HashMap<>();

    public static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            8,
            16,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(512),
            ThreadUtil.newNamedThreadFactory("laokou-oss-service",true),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    static {
        // 其中images,flashs,medias,files,对应文件夹名称,对应dirName
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        extMap.put("images", "gif,GIF,jpg,JPG,jpeg,JPEG,png,PNG,bmp,BMP");
        extMap.put("flashs", "swf,SWF,flv,FLV");
        extMap.put("medias", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,SWF,FLV,MP3,WAV,WMA,WMV,MID,AVI,MPG,ASF,RM,RMVB");
        extMap.put("files", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,DOC,DOCX,XLS,XLSX,PPT,HTM,HTML,TXT,ZIP,RAR,GZ,BZ2");
        extMap.put("sensitive", "txt,TXT");
        extMap.put("pdf", "pdf");
    }


    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * nio上传文件
     * @param rootPath 根目录
     * @param directoryPath 文件相对目录
     * @param inputStream 文件流
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param chunkSize 文件分片
     */
    public static void nioRandomFileChannelUpload(final String rootPath,final String directoryPath,final String fileName,final InputStream inputStream,final Long fileSize,final Long chunkSize) {
        //读通道
        FileChannel inChannel = null;
        try {
            log.info("文件传输开始...");
            //新建目录
            final File newFile = uploadBefore(rootPath,directoryPath,fileName);
            //文件通道
            inChannel = ((FileInputStream)inputStream).getChannel();
            //需要分多少个片
            final Long chunkCount = (fileSize / chunkSize) + (fileSize % chunkSize == 0 ? 0 : 1);
            //同步工具，允许1或N个线程等待其他线程完成执行
            final CountDownLatch latch = new CountDownLatch(chunkCount.intValue());
            //position 指针 > 读取或写入的位置
            for (long index = 0, position = 0, finalEndSize = position + chunkSize ; index < chunkCount; index++,position = index * chunkSize) {
                //指定位置
                final Long finalPosition = position;
                //读通道
                final FileChannel finalInChannel = inChannel;
                executorService.execute(new RandomFileChannelRun(finalPosition,finalEndSize, fileSize, newFile, finalInChannel,latch));
            }
            //等待其他线程
            latch.await();
            //关闭线程池
            log.info("文件传输结束...");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭通道
                closeStream(inputStream,inChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件
     * @param rootPath 根目录
     * @param directoryPath 文件相对目录
     * @param inputStream 文件流
     * @param fileName 文件名
     */
    public static void fileUpload(String rootPath,String directoryPath,String fileName,InputStream inputStream) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            File newFile = uploadBefore(rootPath, directoryPath, fileName);
            fileOutputStream = new FileOutputStream(newFile);
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            log.error("error：【{}】",e.getMessage());
        } finally {
            closeStream(fileOutputStream,inputStream);
        }
    }

    /**
     * 关闭文件流
     * @param closeables 流数组
     * @throws IOException
     */
    public static void closeStream(Closeable...closeables) throws IOException {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                closeable.close();
            }
        }
    }

    /**
     * 创建文件
     * @param rootPath 根目录
     * @param directoryPath 文件相对目录
     * @param fileName 文件名
     */
    private static File uploadBefore(String rootPath,String directoryPath,String fileName) throws IOException {
        File directoryFile = new File(rootPath + directoryPath);
        if (!directoryFile.exists()){
            directoryFile.mkdirs();
        }
        File newFile = new File(directoryFile,fileName);
        if (!newFile.exists()){
            newFile.createNewFile();
        }
        return newFile;
    }

    /**
     * 将文件路径规则化，去掉其中多余的/和\，去掉可能造成文件信息泄漏的../
     */
    public static String normalizePath(String path) {
        path = path.replace('\\', '/');
        path = FileUtil.replaceEx(path, "../", "/");
        path = FileUtil.replaceEx(path, "./", "/");
        if (path != null && path.endsWith("..")) {
            path = path.substring(0, path.length() - 2);
        }
        if (path != null) {
            path = path.replaceAll("/+", "/");
        }
        return path;
    }

    public static File normalizeFile(File f) {
        String path = f.getAbsolutePath();
        path = normalizePath(path);
        return new File(path);
    }


    /**
     * 以指定编码读取指定URL中的文本
     */
    public static String readURLText(String urlPath, String encoding) {
        BufferedReader in =null;
        try {
            URL url = new URL(urlPath);
            in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        return null;
    }

    @SneakyThrows
    public static InputStream getInputStream(String uri) {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        conn.disconnect();
        return conn.getInputStream();
    }

    /**
     * 删除文件，不管路径是文件还是文件夹，都删掉。<br>
     * 删除文件夹时会自动删除子文件夹。
     */
    public static boolean delete(String path) {
        path = normalizePath(path);
        File file = new File(path);
        return delete(file);
    }

    /**
     * 删除文件，不管路径是文件还是文件夹，都删掉。<br>
     * 删除文件夹时会自动删除子文件夹。
     */
    public static boolean delete(File f) {
        f = normalizeFile(f);
        if (!f.exists()) {
            log.info("文件或文件夹不存在：" + f);
            return false;
        }
        if (f.isFile()) {
            return f.delete();
        } else {
            return FileUtil.deleteDir(f);
        }
    }

    /**
     * 删除文件夹及其子文件夹
     */
    private static boolean deleteDir(File dir) {
        dir = normalizeFile(dir);
        try {
            return deleteFromDir(dir) && dir.delete(); // 先删除完里面所有内容再删除空文件夹
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件夹里面的所有文件和子文件夹,但不删除自己本身
     *
     * @return
     */
    public static boolean deleteFromDir(File dir) {
        dir = normalizeFile(dir);
        if (!dir.exists()) {
            log.info("文件夹不存在：" + dir);
            return false;
        }
        if (!dir.isDirectory()) {
            log.info(dir + "不是文件夹");
            return false;
        }
        File[] tempList = dir.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            log.info("删除："+dir);
            if (!delete(tempList[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从指定位置复制文件到另一个文件夹
     */
    public static boolean copy(String oldPath, String newPath) {
        oldPath = normalizePath(oldPath);
        newPath = normalizePath(newPath);
        File oldFile = new File(oldPath);
        return copy(oldFile, newPath);
    }

    public static boolean copy(File oldFile, String newPath) {
        oldFile = normalizeFile(oldFile);
        newPath = normalizePath(newPath);
        if (!oldFile.exists()) {
            log.info("文件或者文件夹不存在：" + oldFile);
            return false;
        }
        if (oldFile.isFile()) {
            return copyFile(oldFile, newPath);
        } else {
            return copyDir(oldFile, newPath);
        }
    }

    /**
     * 复制单个文件
     */
    private static boolean copyFile(File oldFile, String newPath) {
        oldFile = normalizeFile(oldFile);
        newPath = normalizePath(newPath);
        if (!oldFile.exists()) { // 文件存在时
            log.info("文件不存在：" + oldFile);
            return false;
        }
        if (!oldFile.isFile()) { // 文件存在时
            log.info(oldFile + "不是文件");
            return false;
        }
        if(oldFile.getName().equalsIgnoreCase("Thumbs.db")){
            log.info(oldFile + "忽略此文件");
            return true;
        }

        InputStream inStream = null;
        FileOutputStream fs =null;
        try {
            int byteread = 0;
            inStream = new FileInputStream(oldFile); // 读入原文件
            File newFile = new File(newPath);
            //如果新文件是一个目录，则创建新的File对象
            if(newFile.isDirectory()){
                newFile = new File(newPath,oldFile.getName());
            }
            fs = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }

        } catch (Exception e) {
            log.info("复制单个文件" + oldFile.getPath() + "操作出错。错误原因:" + e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            if (inStream != null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fs !=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 复制整个文件夹内容
     */
    private static boolean copyDir(File oldDir, String newPath) {
        oldDir = normalizeFile(oldDir);
        newPath = normalizePath(newPath);
        if (!oldDir.exists()) { // 文件存在时
            log.info("文件夹不存在：" + oldDir);
            return false;
        }
        if (!oldDir.isDirectory()) { // 文件存在时
            log.info(oldDir + "不是文件夹");
            return false;
        }
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File[] files = oldDir.listFiles();
            File temp = null;
            for (int i = 0; i < files.length; i++) {
                temp = files[i];
                if (temp.isFile()) {
                    if (!FileUtil.copyFile(temp, newPath + "/" + temp.getName())) {
                        return false;
                    }
                } else if (temp.isDirectory()) {// 如果是子文件夹
                    if (!FileUtil.copyDir(temp, newPath + "/" + temp.getName())) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.info("复制整个文件夹内容操作出错。错误原因:" + e.getMessage());
//			e.printStackTrace();
            return false;
        }
    }

    /**
     * 将一个字符串中的指定片段全部替换，替换过程中不进行正则处理。<br>
     * 使用String类的replaceAll时要求片段以正则表达式形式给出，有时较为不便，可以转为采用本方法。
     */
    public static String replaceEx(String str, String subStr, String reStr) {
        if (str == null) {
            return null;
        }
        if (subStr == null || subStr.equals("") || subStr.length() > str.length() || reStr == null) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        int lastIndex = 0;
        while (true) {
            int index = str.indexOf(subStr, lastIndex);
            if (index < 0) {
                break;
            } else {
                sb.append(str.substring(lastIndex, index));
                sb.append(reStr);
            }
            lastIndex = index + subStr.length();
        }
        sb.append(str.substring(lastIndex));
        return sb.toString();
    }

    /**
     * 复制单个文件，如果目标文件存在，则不覆盖
     * @param srcFileName 待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return FileUtil.copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 复制单个文件
     * @param srcFileName 待复制的文件名
     * @param descFileName 目标文件名
     * @param coverlay 如果目标文件已存在，是否覆盖
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName,
                                        String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (coverlay) {
                log.debug("目标文件已存在，准备删除!");
                if (!delFile(descFileName)) {
                    log.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else {
                log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                log.debug("目标文件所在的目录不存在，创建目录!");
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    log.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }
        // 准备复制文件
        // 读取的位数
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            log.debug("复制单个文件 " + srcFileName + " 到" + descFileName
                    + "成功!");
            return true;
        } catch (Exception e) {
            log.debug("复制文件失败：" + e.getMessage());
            return false;
        } finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个目录的内容，如果目标目录存在，则不覆盖
     * @param srcDirName 源目录名
     * @param descDirName 目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return FileUtil.copyDirectoryCover(srcDirName, descDirName,
                false);
    }

    /**
     * 复制整个目录的内容
     * @param srcDirName 源目录名
     * @param descDirName 目标目录名
     * @param coverlay 如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName,
                                             String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (coverlay) {
                // 允许覆盖目标目录
                log.debug("目标目录已存在，准备删除!");
                if (!FileUtil.delFile(descDirNames)) {
                    log.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                return false;
            }
        } else {
            // 创建目标目录
            log.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                log.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = FileUtil.copyFile(files[i].getAbsolutePath(),
                        descDirName + files[i].getName());
                // 如果拷贝文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                flag = FileUtil.copyDirectory(files[i]
                        .getAbsolutePath(), descDirName + files[i].getName());
                // 如果拷贝目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     *
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return FileUtil.deleteFile(fileName);
            } else {
                return FileUtil.deleteDirectory(fileName);
            }
        }
    }

    /**
     *
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                log.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     *
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i]
                        .getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 压缩文件或目录
     * @param srcDirName 压缩的根目录
     * @param fileName 根目录下的待压缩的文件名或文件夹名，其中*或""表示跟目录下的全部文件
     * @param descFileName 目标zip文件
     */
    public static void zipFiles(String srcDirName, String fileName,
                                String descFileName) {
        // 判断目录是否存在
        if (srcDirName == null) {
            log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            return;
        }
        File fileDir = new File(srcDirName);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            return;
        }
        String dirPath = fileDir.getAbsolutePath();
        File descFile = new File(descFileName);
        ZipOutputStream zouts =null;
        try {
            zouts = new ZipOutputStream(new FileOutputStream(
                    descFile));
            if ("*".equals(fileName) || "".equals(fileName)) {
                FileUtil.zipDirectoryToZipFile(dirPath, fileDir, zouts);
            } else {
                File file = new File(fileDir, fileName);
                if (file.isFile()) {
                    FileUtil.zipFilesToZipFile(dirPath, file, zouts);
                } else {
                    FileUtil.zipDirectoryToZipFile(dirPath, file, zouts);
                }
            }

            log.info(descFileName + " 文件压缩成功!");
        } catch (Exception e) {
            log.debug("文件压缩失败：" + e.getMessage());
            e.printStackTrace();
        }finally {
            if (zouts!=null){
                try {
                    zouts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将目录压缩到ZIP输出流
     * @param dirPath 目录路径
     * @param fileDir 文件信息
     * @param zouts 输出流
     */
    public static void zipDirectoryToZipFile(String dirPath, File fileDir,
                                             ZipOutputStream zouts) {
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            // 空的文件夹
            if (files.length == 0) {
                // 目录信息
                ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
                try {
                    zouts.putNextEntry(entry);
                    zouts.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    // 如果是文件，则调用文件压缩方法
                    FileUtil
                            .zipFilesToZipFile(dirPath, files[i], zouts);
                } else {
                    // 如果是目录，则递归调用
                    FileUtil.zipDirectoryToZipFile(dirPath, files[i],
                            zouts);
                }
            }

        }

    }

    /**
     * 将文件压缩到ZIP输出流
     * @param dirPath 目录路径
     * @param file 文件
     * @param zouts 输出流
     */
    public static void zipFilesToZipFile(String dirPath, File file,
                                         ZipOutputStream zouts) {
        FileInputStream fin = null;
        ZipEntry entry = null;
        // 创建复制缓冲区
        byte[] buf = new byte[4096];
        int readByte = 0;
        if (file.isFile()) {
            try {
                // 创建一个文件输入流
                fin = new FileInputStream(file);
                // 创建一个ZipEntry
                entry = new ZipEntry(getEntryName(dirPath, file));
                // 存储信息到压缩文件
                zouts.putNextEntry(entry);
                // 复制字节到压缩文件
                while ((readByte = fin.read(buf)) != -1) {
                    zouts.write(buf, 0, readByte);
                }
                zouts.closeEntry();
                fin.close();
                log.info("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (fin!=null) {
                    try {
                        fin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
     * @param dirPath 目录名
     * @param file entry文件名
     * @return
     */
    private static String getEntryName(String dirPath, File file) {
        String dirPaths = dirPath;
        if (!dirPaths.endsWith(File.separator)) {
            dirPaths = dirPaths + File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
        if (file.isDirectory()) {
            filePath += "/";
        }
        int index = filePath.indexOf(dirPaths);

        return filePath.substring(index + dirPaths.length());
    }

    /**
     * 修复路径，将 \\ 或 / 等替换为 File.separator
     * @param path
     * @return
     */
    public static String path(String path){
        String p = StringUtils.replace(path, "\\", "/");
        p = StringUtils.join(StringUtils.split(p, "/"), "/");
        if (!StringUtils.startsWithAny(p, "/") && StringUtils.startsWithAny(path, "\\", "/")){
            p += "/";
        }
        if (!StringUtils.endsWithAny(p, "/") && StringUtils.endsWithAny(path, "\\", "/")){
            p = p + "/";
        }
        return p;
    }

    private static class RandomFileChannelRun extends Thread {
        //写通道
        private File newFile;
        //读通道
        private FileChannel srcChannel;
        //读取或写入的位置
        private long position;
        //结束位置
        private long endSize;
        //计数器
        private CountDownLatch latch;
        //文件大小
        private Long fileSize;

        RandomFileChannelRun(final Long position,final Long endSize,final Long fileSize,final File newFile, final FileChannel srcChannel,final CountDownLatch latch) {
            this.newFile = newFile;
            this.srcChannel = srcChannel;
            this.fileSize = fileSize;
            this.latch = latch;
            this.endSize = endSize;
            this.position = position;
        }

        @SneakyThrows
        @Override
        public void run() {
            //结束位置
            if (endSize > fileSize) {
                endSize = fileSize;
            }
            //随机文件读取
            RandomAccessFile accessFile = new RandomAccessFile(newFile, RW);
            //写通道
            FileChannel newChannel = accessFile.getChannel();
            //标记位置
            newChannel.position(position);
            //零拷贝
            //transferFrom 与 transferTo 区别
            //transferTo 最多拷贝2gb，和源文件大小保持一致
            //transferFrom 每个线程拷贝20MB
            srcChannel.transferTo(position,endSize,newChannel);
            //减一，当为0时，线程就会执行
            latch.countDown();
            //关闭流
            closeStream(accessFile,newChannel);
        }
    }
}
