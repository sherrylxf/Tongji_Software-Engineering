# 小型宾馆管理信息系统 - 用例分析

## 项目说明

本项目包含小型宾馆管理信息系统的用例分析和用例图。

## 文件说明

- **用例分析文档.md** - 详细的用例分析文档，包含所有用例的详细描述
- **用例图.puml** - PlantUML格式的用例图源文件
- **类图.puml** - PlantUML格式的类图源文件
- **类图.md** - Mermaid格式的类图源文件
- **类图说明文档.md** - 类图的详细说明文档
- **状态图.md** - Mermaid格式的状态图（包含订单、客房、支付状态图）
- **状态图.puml** - PlantUML格式的状态图汇总文件
- **状态图-订单.puml** - 订单状态图
- **状态图-客房业务状态.puml** - 客房业务状态图
- **状态图-客房健康状态.puml** - 客房健康状态图
- **状态图-客房综合状态.puml** - 客房综合状态图（业务状态+健康状态）
- **状态图-支付.puml** - 支付状态图
- **需求检查清单.md** - 需求覆盖情况检查清单
- **README.md** - 本说明文件

## 如何查看用例图

### 方法一：使用在线工具
1. 访问 [PlantUML在线编辑器](http://www.plantuml.com/plantuml/uml/)
2. 打开 `用例图.puml` 文件
3. 复制内容到在线编辑器
4. 查看生成的用例图

### 方法二：使用VS Code插件
1. 安装 PlantUML 插件
2. 打开 `用例图.puml` 文件
3. 按 `Alt+D` 预览用例图

### 方法三：使用命令行工具
```bash
# 安装PlantUML（需要Java环境）
# 下载 plantuml.jar

# 生成PNG图片
java -jar plantuml.jar 用例图.puml

# 生成SVG图片
java -jar plantuml.jar -tsvg 用例图.puml
```

## 系统概述

小型宾馆管理信息系统包含以下四个主要功能模块：

1. **客房信息管理** - 管理客房基本信息、状态等
2. **客房设备管理** - 管理客房设备、家具、设施等
3. **客房预订管理** - 管理客房预订订单
4. **客房业务管理** - 管理入住、退房等业务

## 主要参与者

- **订房人** - 预订客房的用户
- **旅客** - 实际入住的客人
- **前台服务员** - 处理前台业务
- **客房服务员** - 负责客房清洁维护
- **宾馆经理** - 管理宾馆运营
- **系统管理员** - 系统维护人员

## 用例统计

- 总用例数：17个
- 订房管理用例：6个
- 业务管理用例：5个
- 客房管理用例：3个
- 系统管理用例：3个

## 类图说明

类图采用三层架构设计：
- **实体层**：包含User、Customer、Guest、Room、Order、Eqpment等核心实体类
- **控制层**：包含AuthenticationController、ReservationController、CheckInController等业务控制类
- **边界层**：包含WebInterface、ReceptionInterface、ManagementInterface等用户界面类

### 查看类图

**PlantUML格式**：
- 在线查看：访问 [PlantUML在线编辑器](http://www.plantuml.com/plantuml/uml/)，复制 `类图.puml` 的内容
- VS Code：安装 PlantUML 插件，打开 `类图.puml` 文件预览

**Mermaid格式**：
- 在线查看：访问 [Mermaid在线编辑器](https://mermaid.live/)，复制 `类图.md` 中的mermaid代码块内容
- VS Code：安装 Mermaid Preview 插件，打开 `类图.md` 文件预览
- GitHub/GitLab：直接在Markdown文件中查看（原生支持Mermaid）

详细说明请参考 **类图说明文档.md**

