package org.example.krpc.api.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {//实现Serializable接口，使对象可以序列化
    //客户端和服务端共有
    private Integer id;
    private String userName;
    //这里默认是男，女为false
    private Boolean sex;
}