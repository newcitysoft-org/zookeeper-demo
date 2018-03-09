package com.newcitysoft.study.zookeeper.module.net.scene.socket.entity;

/**
 * Socket数据传输协议包
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/9 10:42
 */
public class DataPacket<T> {

    public enum PacketType {
        /**
         * 同步获取
         */
        SYNCGET("syncGet"),
        /**
         * 异步获取
         */
        ASYNCGET("asyncGet"),
        /**
         *
         */
        REPORT("report"),
        /**
         *
         */
        SEND("send");

        private String type;
        private PacketType(String type) {
            this.type = type;
        }

        /**
         * 根据类型的名称，返回类型的枚举实例。
         *
         * @param typeName 类型名称
         */
        public static PacketType fromTypeName(String typeName) {
            for (PacketType type : PacketType.values()) {
                if (type.getType().equals(typeName)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "PacketType{" +
                    "type='" + type + '\'' +
                    '}';
        }

        public String getType() {
            return this.type;
        }
    }

    private PacketType type;
    private int length;
    private T body;

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
