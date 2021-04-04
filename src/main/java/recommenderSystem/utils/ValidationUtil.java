package recommenderSystem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * 工具类校验信息
 * @version v1.0.0
 * @Author xiayu
 * @Date 2020/1/10 9:53
 */
public class ValidationUtil {
    /**
     * 校验手机号
     * @param tel
     * @return
     */
    public static boolean validationTel(String tel) {
        String phone = "13123456789";
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if (phone.length() != 11) {
            //  System.out.println("手机号应为11位数");
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (isMatch) {
                //  System.out.println("您的手机号" + phone + "是正确格式@——@");
            } else {
                // System.out.println("您的手机号" + phone + "是错误格式！！！");
                return false;
            }
        }
        return true;
    }
}
