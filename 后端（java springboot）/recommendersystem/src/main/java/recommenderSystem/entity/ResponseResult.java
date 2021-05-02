package recommenderSystem.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *      通用数据传输对象
 * @version v1.0.0
 * @Author wqd
 * @Date 2020/10/1 12:10
 */
@Data
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 3468352004150968551L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回对象
     */
    private T data;

    /**
     * 总记录数
     */
    private Integer total = 0;
    /**
     * 总页数
     */
    private Integer pageTotal = 0;


    public ResponseResult() {
        super();
    }

    public ResponseResult(Integer code) {
        super();
        this.code = code;
    }

    public ResponseResult(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, Throwable throwable) {
        super();
        this.code = code;
        this.message = throwable.getMessage();
    }

    public ResponseResult(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    /**
     * 分页
     * @param code  编码
     * @param message 消息
     * @param data  数据
     * @param total   总数据量
     * @param pageTotal  共几页
     */
    public ResponseResult(Integer code, String message, T data, Integer total, Integer pageTotal  ) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
        this.pageTotal = pageTotal;
    }

    public ResponseResult(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ResponseResult<?> other = (ResponseResult<?>) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }

    /**
     * 通用状态码
     * <p>
     * Description:
     * </p>
     *
     */
    public class CodeStatus {
        /**
         * 请求成功
         */
        public static final int OK = 20000;
        /**
         * 请求成功 插入
         */
        public static final int OK_INSERT = 20001;

        /**
         * 请求失败
         */
        public static final int FAIL = 20002;

        /**
         * 熔断请求
         */
        public static final int BREAKING = 20004;

        /**
         * 服务器异常
         */
        public static final int UNKNOWN_EXCEPTION = 50000;

        /**
         * 自定义异常
         */
        public static final int CUSTOM_EXCEPTION = 51000;

        /**
         * 非法请求
         */
        public static final int ILLEGAL_REQUEST = 50001;

        /**
         * 非法令牌
         */
        public static final int ILLEGAL_TOKEN = 50008;

        /**
         * 其他客户登录
         */
        public static final int OTHER_CLIENTS_LOGGED_IN = 50012;

        /**
         * 令牌已过期
         */
        public static final int TOKEN_EXPIRED = 50014;
    }
}

