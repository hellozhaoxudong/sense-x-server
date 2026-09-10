FROM registry.cn-shanghai.aliyuncs.com/sense-ai/sense-tool:jdk22

## 设置 TZ 时区
ENV TZ=Asia/Shanghai

EXPOSE 9600

# 复制加密后的数据平台jar、jar包解密工具
COPY target/app.jar /app/

ENV SPRING_ENV=prod
ENTRYPOINT ["java", "-jar","/app/app.jar"]