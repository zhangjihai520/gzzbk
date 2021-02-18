package com.ry.common.utils.file;

/**
 * 媒体类型工具类
 *
 * @author
 */
public class MimeTypeUtils {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] USERFACE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] IMAGE_EXTENSION = {"jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"mp4"};

    public static final String[] FILE_EXTENSION = {"mp4"};

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt",
            // 压缩文件
            "rar", "zip",
            // pdf
            "pdf"};

    public static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }

    public static String[] getAllowMimeTypeByFileType(int fileType) {
        switch (fileType) {
            case 1:
                return USERFACE_EXTENSION;
            case 2:
                return IMAGE_EXTENSION;
            case 3:
                return MEDIA_EXTENSION;
            case 4:
                return DEFAULT_ALLOWED_EXTENSION;
            case 5:
                return IMAGE_EXTENSION;
            default:
                return DEFAULT_ALLOWED_EXTENSION;
        }
    }
}
