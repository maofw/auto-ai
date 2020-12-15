package com.ai.generator.util;

import com.ai.generator.model.FileResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by maofw on 2019-04-01.
 */
public class FileUploadUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String uploadPath ="upload" ;

    /**
     * 上传返回照片对象
     * @param file
     * @return
     */
    public static FileResponse upload(String imageUploadPathRoot , MultipartFile file) throws IOException {
        //获得物理路径webapp所在路径
        String name = null;
        String path = null;
        FileResponse myFile = null ;
        if(!file.isEmpty()){
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //获得文件类型（可以判断如果不是图片，禁止上传）
//            String contentType=file.getContentType();
            //获得文件类型（可以判断如果不是图片，禁止上传）
            name = file.getOriginalFilename();
            //获得文件后缀名称
            String dateDir = sdf.format(new Date());
            String subfix=name.substring(name.lastIndexOf(".")+1);
            //生成的文件名称
            String picName = uuid+"."+subfix;
            //http地址相对路径
            path = uploadPath + "/" + dateDir + "/" + picName;
            //文件上传路径
            String imageDirStr = uploadPath + java.io.File.separator + dateDir;
            java.io.File imageDir = new java.io.File(imageUploadPathRoot+imageDirStr);
            if(!imageDir.exists()){
                imageDir.mkdirs();
            }
            //上传写入文件内容
            file.transferTo(new java.io.File(imageDir,picName));

            myFile = new FileResponse();
            myFile.setName(name);
            myFile.setPath(path);
            myFile.setSubfix(subfix);
            myFile.setSize(file.getSize());
            myFile.setContentType(file.getContentType());
            myFile.setCreateTime(sdf_db.format(new Date()));
        }
        return myFile ;
    }

    public static boolean deleteImageOnDisk(String filePath){
        if(filePath!=null && !StringUtils.isEmpty(filePath)){
            java.io.File realFile = new java.io.File(filePath);
            if(realFile.exists()){
                return realFile.delete();
            }
        }
        return false;
    }

    public static java.io.File getFileOnDisk( String filePath){
        if(filePath!=null && !StringUtils.isEmpty(filePath)){
            java.io.File realFile = new java.io.File(filePath);
            if(realFile.exists()){
                return realFile;
            }
        }
        return null;
    }


    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        java.io.File file = getFileOnDisk(pathStr);
        if(file!=null){
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                byte[] data = bos.toByteArray();
                bos.close();
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
