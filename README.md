# 邮件系统后端项目

### SMTP服务测试流程

- 启动SpringBootApplication类，启动了服务器和服务器中的SMTP服务
- 进入Client包，启动该包下的SmtpClient，也就是Smtp的客户端测试程序

### SMTP 测试样例

- 在控制台输入红框中的指令：

  ![image-20210512233140580](https://shuairun.oss-cn-beijing.aliyuncs.com/img/image-20210512233140580.png)



### POP3 测试样例

![image-20210514005722018](https://gitee.com/zfbz/pics/raw/master/20210514005734.png)

### 查看接口文档

- 启动项目，在浏览器输入地址http://localhost:8080/swagger-ui.html 即可查看接口文档

  ![image-20210517200811285](https://shuairun.oss-cn-beijing.aliyuncs.com/img/image-20210517200811285.png)

### SMTP代理服务器测试样例

#### 发送邮件

- url:`ws://127.0.0.1:8080/smtp`

- 先自定义WebSocket的头部信息：username和password

- 发送`json字符串`

  ```json
  {
  "senderEmail":"",
  "receiverEmail":"",
  "sendTime": "Date类型",
  "subject":"",
  "body":""
  }
  ```

- 返回`json字符串`

  - 发送成功:

    ```JSON
    {
     "state":"200",
     "message":"successful",
     "body":null
    }
    ```

  - 发送失败：

    ```json
    {
     "state":"400",
     "message":"failed",
     "body":null
    }
    ```

### POP3代理服务器测试样例

#### 获取邮件

- url :ws://127.0.0.1:8080/pop3

- 先自定义WebSocket的头部信息：username和password

- 发送`GET`，获取邮件信息，返回的`json字符串`如下：

  ```json
  {
      "body": [
          {
              "body": "当你收到这封信的时候,你也许将会从这个世界消失一段时间",
              "receiverEmail": "lh@lyq.com",
              "sendTime": "2020-05-20",
              "senderEmail": "qhr667@lyq.com",
              "subject": "群发邮件请不要回复"
          },
          {
              "body": "当你收到这封信的时候,你也许将会从这个世界消失一段时间",
              "receiverEmail": "lh@lyq.com",
              "sendTime": "2020-05-20",
              "senderEmail": "qhr667@lyq.com",
              "subject": "群发邮件请不要回复"
          }
      ],
      "message": "success",
      "state": 200
  }
  ```

#### 删除邮件

- 发送指令:DELE指令

  ```
  DELE 邮件序号
  ```

- 返回的json字符串:

  ```json
  {
   "state":"200",
   "message":"successful",
   "body":null
  }
  ```

#### 取消删除

- 发送指令:REST指令

  ```
  REST 邮件序号
  ```

- 返回的json字符串:

  ```json
  {
   "state":"200",
   "message":"successful",
   "body":null
  }
  ```

#### 退出POP3服务

- 发送`QUIT`指令

- 返回json字符串：

  ```json
  {
   "state":"200",
   "message":"successful",
   "body":null
  }
  ```

