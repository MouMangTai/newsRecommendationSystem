package recommenderSystem.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Description:
 *工具类  文件操作
 * @version v1.0.0
 * @Author xiayu
 * @Date 2019/12/30 17:54
 */
public class FileUtil {
    /**
     * 获取下载文件名称
     * @param fileName
     * @param userAgent 可以用        HttpServletRequest request = requestAttributes.getRequest();
     *                                 String agent = request.getHeader("USER-AGENT"); 来生成
     * @return
     * @throws Exception
     */
    public static String getDownloadEncodeFileName(String fileName, String userAgent) throws Exception{
        // 进行转码，使其支持中文文件名
        String codedFileName = null;
        // 解决中文文件名乱码问题
        if (userAgent.toLowerCase().indexOf("firefox") > 0) {
            codedFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
        } else if (userAgent.toUpperCase().indexOf("MSIE") > 0) {
            codedFileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        } else if (userAgent.toUpperCase().indexOf("CHROME") > 0) {
            codedFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// 谷歌
        } else {
            codedFileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        }
        return codedFileName;
    }
    /**
     * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName:指定的文件全路径名 strDestDir: 移动到指定的文件夹 返回值: 如果成功true;否则false
     *
     * @param strSourceFileName
     * @param strDestDir
     * @return
     * testParameter copyTo("C:/Users/xiayu/Desktop/测试文件夹/测试文件.txt", "C:/Users/xiayu/Desktop");
     */

    public static boolean copyTo(String strSourceFileName, String strDestDir) {
        File fileSource = new File(strSourceFileName);
        File fileDest = new File(strDestDir);

        // 如果源文件不存或源文件是文件夹
        boolean exists = fileSource.exists();
        boolean file = fileSource.isFile();
        if (!fileSource.exists() || !fileSource.isFile()) {
            // logger.debug("源文件[" + strSourceFileName + "],不存在或是文件夹!");
            return false;
        }

        // 如果目标文件夹不存在
        if (!fileDest.isDirectory() || !fileDest.exists()) {
            if (!fileDest.mkdirs()) {
                //  logger.debug("目录文件夹不存，在创建目标文件夹时失败!");
                return false;
            }
        }

        try {
            String strAbsFilename = strDestDir + File.separator + fileSource.getName();

            FileInputStream fileInput = new FileInputStream(strSourceFileName);
            FileOutputStream fileOutput = new FileOutputStream(strAbsFilename);

            // logger.debug("开始拷贝文件");

            int count = -1;

            long nWriteSize = 0;
            long nFileSize = fileSource.length();

            byte[] data = new byte[1024];

            while (-1 != (count = fileInput.read(data, 0, 1024))) {

                fileOutput.write(data, 0, count);

                nWriteSize += count;

                long size = (nWriteSize * 100) / nFileSize;
                long t = nWriteSize;

                String msg = null;

                if (size <= 100 && size >= 0) {
                    msg = "\r拷贝文件进度:   " + size + "%   \t" + "\t   已拷贝:   " + t;
                    // logger.debug(msg);
                } else if (size > 100) {
                    msg = "\r拷贝文件进度:   " + 100 + "%   \t" + "\t   已拷贝:   " + t;
                    //  logger.debug(msg);
                }

            }
            fileInput.close();
            fileOutput.close();
            // logger.debug("拷贝文件成功!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName: 是指定的文件全路径名 strDestDir: 移动到指定的文件夹中 返回值: 如果成功true; 否则false
     *
     * @param strSourceFileName
     * @param strDestDir
     * @return testFarameter  moveFile("C:/Users/xiayu/Desktop/测试文件夹/测试文件.txt", "C:/Users/xiayu/Desktop");
     */
    public static boolean moveFile(String strSourceFileName, String strDestDir) {
        if (copyTo(strSourceFileName, strDestDir)) {
            return delete(strSourceFileName);
        } else {
            return false;
        }
    }
    /**
     * 功 能: 删除指定的文件 参 数: 指定绝对路径的文件名 strFileName 返回值: 如果删除成功true否则false;
     *
     * @param strFileName
     * @return
     * testFarameter b = delete("C:/Users/xiayu/Desktop/测试文件夹/测试文件.txt");
     */
    public static boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);

        if (!fileDelete.exists() || !fileDelete.isFile()) {
            //  logger.debug(strFileName + "不存在!");
            return false;
        }

        return fileDelete.delete();
    }

    public static boolean delete(File file) {
        if(file == null) {
            return true;
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
            return file.delete();
        }
    }



    /**
     * 功 能: 删除文件夹 参 数: strDir 要删除的文件夹名称 返回值: 如果成功true;否则false
     *
     * @param strDir
     * @return
     */
    public static boolean removeDir(String strDir) {
        File rmDir = new File(strDir);
        if (rmDir.isDirectory() && rmDir.exists()) {
            String[] fileList = rmDir.list();

            for (int i = 0; i < fileList.length; i++) {
                String subFile = strDir + File.separator + fileList[i];
                File tmp = new File(subFile);
                if (tmp.isFile()) {
                    tmp.delete();
                } else if (tmp.isDirectory()) {
                    removeDir(subFile);
                }
            }
            rmDir.delete();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 功 能: 创建文件夹 参 数: strDir 要创建的文件夹名称 返回值: 如果成功true;否则false
     *
     * @param strDir
     * @return
     */
    public static boolean makeDir(String strDir) {
        File fileNew = new File(strDir);

        if (!fileNew.exists()) {
            return fileNew.mkdirs();
        } else {
            return true;
        }
    }
    /**
     * 获取文件扩展名
     */
    public static String getFileExtName(String file) {
        if (MapperUtils.isBlank(file)) {
            return ".";
        }
        file = file.replace(File.separator, "/");
        String[] fs = file.split("/");
        if ((fs != null) && (fs.length > 0)) {
            file = fs[(fs.length - 1)];
        }
        int pos = file.lastIndexOf(".");
        if (pos >= 0) {
            return file.substring(pos);
        }
        return ".";
    }
    /**
     * 下载网络文件
     */
    public static void downloadNetFile(String url, String saveDir, String fileName) {
        try (InputStream ins = new URL(url).openStream()) {
            Path target = Paths.get(saveDir, fileName);
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO抛异常
        }
    }
    /**
     * 写入文件
     */

    public static void writeToFile(String file, String data) throws IOException {
        File f = new File(file);
        mkdirs(file);
        if (!f.exists()) {
            mkdirs(file);
            f.createNewFile();
        }
        RandomAccessFile fil = null;
        try {
            fil = new RandomAccessFile(f, "rw");
            long fileLength = fil.length();
            fil.seek(fileLength);
            fil.write(data.getBytes("UTF-8"));
        } finally {
            if (fil != null) {
                fil.close();
            }
        }
    }
    /**
     * 创建文件
     */
    public static void mkdirs(String file) {
        File f = new File(file);
        if (!f.exists()) {
            f.mkdirs();
        }
    }
    /**
     * 读取远程文件
     */
    public static String readRemoteFile(String htmlUrl) {
        int HttpResult; // 服务器返回的状态
        String ee = new String();
        InputStreamReader isReader = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(htmlUrl); // 创建URL
            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
            urlconn.connect();
            HttpURLConnection httpconn = (HttpURLConnection) urlconn;
            HttpResult = httpconn.getResponseCode();
            if (HttpResult != HttpURLConnection.HTTP_OK) {
                return null;
            } else {
                isReader = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
                reader = new BufferedReader(isReader);
                StringBuffer buffer = new StringBuffer();
                String line; // 用来保存每行读取的内容
                line = reader.readLine(); // 读取第一行
                while (line != null) { // 如果 line 为空说明读完了
                    buffer.append(line); // 将读到的内容添加到 buffer 中
                    buffer.append(" "); // 添加换行符
                    line = reader.readLine(); // 读取下一行
                }
                ee = buffer.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isReader != null) {
                try {
                    isReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ee;
    }
    public static String readRemoteFile(String htmlUrl, String charsetName) {

        int HttpResult; // 服务器返回的状态
        String ee = new String();
        InputStreamReader isReader = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(htmlUrl); // 创建URL
            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
            urlconn.connect();
            HttpURLConnection httpconn = (HttpURLConnection) urlconn;
            HttpResult = httpconn.getResponseCode();
            if (HttpResult != HttpURLConnection.HTTP_OK) {
                return null;
            } else {
                isReader = new InputStreamReader(urlconn.getInputStream(), charsetName);
                reader = new BufferedReader(isReader);
                StringBuffer buffer = new StringBuffer();
                String line; // 用来保存每行读取的内容
                line = reader.readLine(); // 读取第一行
                while (line != null) { // 如果 line 为空说明读完了
                    buffer.append(line); // 将读到的内容添加到 buffer 中
                    buffer.append(" "); // 添加换行符
                    line = reader.readLine(); // 读取下一行
                }
                ee = buffer.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isReader != null) {
                try {
                    isReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ee;
    }

}
