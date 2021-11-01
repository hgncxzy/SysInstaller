升级流程：

后台推送升级 ->前端下载 APK -> 下载完成后发送广播给安装器（本项目）-->本安装器执行安装操作 ->安装完成 ->发送广播给升级后的 APP ->升级后的 APP 被拉起。

系统签名步骤：

1. 在 app 目录下面创建 script 目录，依次创建不同系统平台的文件夹，项目中是 _flavors*，然后放入系统方给定的 platform.pk8 、platform.x509.pem、signapk.jar 文件 （具体目录结构详见源码 app/script ）。

2. app gradle 下面编写脚本文件

   ```groovy
   task buildSignApk() {
       dependsOn << build
   }
   
   //调用java中的main
   task signApk_flavors1(type: JavaExec) {
       dependsOn << buildSignApk
       classpath(files('script/signapk.jar'))
       main 'com.android.signapk.SignApk'
       args "script/_flavors1/platform.x509.pem",
          "script/_flavors1/platform.pk8",
          'build/outputs/apk/release/app-release-unsigned.apk',
          'build/outputs/apk/release/app-release-flavors1.apk'
   }
   // 其他平台的大包文件脚本 ...
   ```

   依次定义的是 SignApk 路径、platform.x509.pem 路径、platform.pk8 路径、未签名包路径、签名包路径。

3. 点击左侧箭头运行脚本，即可在制定目录下生成签名后的 app。