# code-generator-all

下载：https://github.com/lcmatrix-code-generator/code-generator-all/releases

## 这是一个java语言实现代码生成器，但并不限于生成java代码，事实上，通过插件扩展，它几乎可以完成任何类型文件生成的工作。
## 特点：
* 自带UI，虽然很丑，但是够用，总之比命令行易用点；
* 支持插件，可以通过插件扩展数据源和引擎支持，目前已实现的插件有：[数据库源插件](https://github.com/lcmatrix-code-generator/db-source-plugin)、[freemarker模板引擎插件](https://github.com/lcmatrix-code-generator/freemarker-template-engine-plugin)、[velocity模板引擎插件](https://github.com/lcmatrix-code-generator/velocity-template-engine-plugin)；
* 生成代码将保持与模板目录完全一致的目录结构，文件名同样也支持通过引擎生成，以此来适应各类代码千变万化的目录结构和文件命名规则。
