# code-generator-all
下载：https://github.com/lcmatrix-code-generator/code-generator-all/releases   （运行需要先安装jdk8，https://www.oracle.com/java/technologies/javase-jdk8-downloads.html）

## 这是一个java语言实现代码生成器，但并不限于生成java代码，事实上，通过插件扩展，它几乎可以完成任何文件生成类的工作。
## 特点：
* 自带UI，虽然很丑，但是够用，总之比命令行易用点；
* 支持插件，可以通过插件扩展数据源和引擎支持，加载插件时自动根据插件定义生成输入表单。目前已实现的插件有：[数据库源插件](https://github.com/lcmatrix-code-generator/db-source-plugin)、[freemarker模板引擎插件](https://github.com/lcmatrix-code-generator/freemarker-template-engine-plugin)、[velocity模板引擎插件](https://github.com/lcmatrix-code-generator/velocity-template-engine-plugin)；
* 生成代码将保持与模板目录完全一致的目录结构，文件名同样也支持通过引擎生成，以此来适应各类代码千变万化的目录结构和文件命名规则。

## 这里有一套基于mybatis-plus的java后端代码freemarker模板，仅供参考，https://github.com/lcmatrix-code-generator/code-generator-all/tree/master/docs/mvc-mybatis-plus-template/

## 自带的UI长这样（使用数据库源插件时）：
![UI](https://github.com/lcmatrix-code-generator/code-generator-all/blob/master/docs/screenshot1.png?raw=true)

## 插件开发文档
 插件有两种，分别是数据源插件和模板引擎插件，它们的作用如下图：
 
 ![plugin](https://github.com/lcmatrix-code-generator/code-generator-all/blob/master/docs/plugin-doc.png?raw=true)
 
 ### 1.准备工作
 下载 [code-generator-all](https://github.com/lcmatrix-code-generator/code-generator-all) 工程到本地，进入 plugin-common 目录，执行命令 `mvn install`
 
 ### 2.数据源插件开发
 建议下载示例工程 [source-plugin-sample](https://github.com/lcmatrix-code-generator/source-plugin-sample) ，在此工程基础上开发。
 
 1. 添加 plugin-common依赖
 
        <dependency>
            <artifactId>plugin-common</artifactId>
            <groupId>top.lcmatrix.util.codegenerator</groupId>
            <version>0.2.2</version>
        </dependency>
 
 2. 定义输入对象（输入表单）
 输入对象其实就是一个简单java对象，主程序会根据输入对象生成输入表单供用户输入，目前支持的数据类型包括：
  * int, short, long, double, float, byte 等基本数据类型 （生成文本输入框）
  * String （生成文本输入框）
  * boolean （生成勾选框）
  * File （生成文件选择器-单选）
  * List<File> （生成文件选择器-多选）
  * enum （要求实现 ISelectableEnum 接口，生成单选下拉框）
  * List<enum> (其中的enum类型要求实现 ISelectableEnum 接口，生成多选框)

  同时，提供一个 @InputField 注解，用于定义更丰富的输入表单选项，如label、默认值、是否必填、验证正则表达式、是否密码框、文件选择范围等。
 
 3. 定义输出对象
 输出对象最终将输出到模板引擎用于生成代码文件，没有什么特殊要求。
 
 4. 定义插件类，实现抽象类 AbstractSourcePlugin<输入, 输出> 即可，根据用户输入，组装输出对象。
 注意：最终输出的是对象列表，每个对象对应一个输出文件。
 
 5. 在 工程根目录/src/main/resources 下建立 `plugin-definition.json` 文件，用于描述该插件，内容示例：
 
      {
        "name": "sample source",      // 插件名称
        "class": "top.lcmatrix.util.codegenerator.plugin.sample.SampleSourcePlugin",     // 插件主类，即实现了 AbstractSourcePlugin 的类
        "version": 1.0     // 插件版本
      }
 
 6. 打包。在工程根目录执行命令 `mvn package` 打包，将target目录下生成的 `with-dependences` jar 包拷贝到代码生成器所在目录的plugins目录下，启动代码生成器，即可以在 select source 下拉框中看到你的插件。
 7. 调试。可以在代码生成器界面查看通过该插件生成的数据模型结构。填写好各输入项后，点击 `preview one of output models` 按钮即可（需要至少有一个输出对象）。另外，可以在代码生成器所在目录的 `logs` 目录下查看详细的日志。
 
 ### 3. 模板引擎插件开发
 建议参考 [freemarker-template-engine-plugin](https://github.com/lcmatrix-code-generator/freemarker-template-engine-plugin) 工程进行开发。
 1. 添加 plugin-common依赖
 
        <dependency>
            <artifactId>plugin-common</artifactId>
            <groupId>top.lcmatrix.util.codegenerator</groupId>
            <version>0.2.2</version>
        </dependency>
 
 2. 定义插件类。实现抽象类 AbstractTemplateEnginePlugin 即可，其中有2个抽象方法需要实现：
 
      public String apply(String s, Object model)            // 用于生成文件名称
      public byte[] apply(File templateFile, Object model)   // 用于生成文件内容
      
 3. 在 工程根目录/src/main/resources 下建立 `plugin-definition.json` 文件，用于描述该插件，内容示例：
 
      {
        "name": "freemarker engine",      // 插件名称
        "class": "top.lcmatrix.util.codegenerator.plugin.freemarker.FreemarkerPlugin",     // 插件主类，即实现了 AbstractTemplateEnginePlugin 的类
        "version": 1.0     // 插件版本
      }
 
 4. 打包。在工程根目录执行命令 `mvn package` 打包，将target目录下生成的 `with-dependences` jar 包拷贝到代码生成器所在目录的plugins目录下，启动代码生成器，即可以在 select template engine 下拉框中看到你的插件。
 5. 调试。可以在代码生成器界面查看输入到该模板引擎插件的数据模型结构。填写好各输入项后，点击 `preview one of output models` 按钮即可（需要至少有一个输出对象）。另外，可以在代码生成器所在目录的 `logs` 目录下查看详细的日志。
