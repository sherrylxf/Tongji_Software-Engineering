# SITP 项目模块接口详细说明

本文档详细说明了 SITP 项目模块（ProjectModule）中 ProjectController 提供的 REST API 接口规范。

## 认证说明

**重要：** 用户访问 SITP 模块的所有接口，必须先通过系统登录获取有效的 `token`，并在请求中携带该 `token` 进行身份验证。未提供有效 `token` 的请求将被拒绝，返回 401 未授权错误。

- 所有接口的 `token` 参数均为**必填**
- `token` 通过系统登录接口获取
- `token` 具有时效性，过期后需要重新登录获取
- 如果 `token` 无效或已过期，接口将返回 `INVALID_TOKEN` 错误

---

## 1. 获取所有项目列表

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/projects`

**参数：**
- `token`（必填）：用户认证令牌，用于身份验证和记录用户访问日志
- `page`（可选，整数）：页码，从1开始，默认为1
- `pageSize`（可选，整数）：每页数量，默认为20，最大为100

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 解析分页参数：`page`（默认1）和 `pageSize`（默认20，最大100）
3. 调用 `ProjectService.getProjects(page, pageSize, userId)` 方法获取分页项目列表
4. `ProjectService` 委托 `ProjectRepository.findAll(page, pageSize)` 从数据库分页查询项目
5. 如果提供了 `userId`，查询该用户对每个项目的选择状态（已选择/未选择）
6. 同时查询总记录数用于分页信息计算
7. 将 `Project` 实体列表转换为列表响应格式（简化字段，包含选择状态）
8. 返回项目列表结果，包含分页信息

**请求示例：**
```json
GET /api/sitp/projects?token=userToken12345&page=1&pageSize=20
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取项目列表成功",
  "data": [
    {
      "projectId": "SITP2026001",
      "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
      "projectType": "创新训练项目",
      "name": "AI赋能的先进电池健康状态快速测评方法",
      "primaryDiscipline": "工学",
      "secondaryDiscipline": "计算机类",
      "college": "计算机科学与技术学院",
      "addedBy": "汪政",
      "addedByAccount": "24510",
      "addedByRole": "指导教师",
      "selectionStatus": "未选择"
    },
    {
      "projectId": "SITP2026002",
      "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
      "projectType": "创新训练项目",
      "name": "AI赋能的嗅觉表征技术",
      "primaryDiscipline": "工学",
      "secondaryDiscipline": "计算机类",
      "college": "计算机科学与技术学院",
      "addedBy": "汪政",
      "addedByAccount": "24510",
      "addedByRole": "指导教师",
      "selectionStatus": "未选择"
    },
    {
      "projectId": "SITP2026003",
      "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
      "projectType": "创新训练项目",
      "name": "面向3D模型的高效跨模态检索研究",
      "primaryDiscipline": "工学",
      "secondaryDiscipline": "计算机类",
      "college": "计算机科学与技术学院",
      "addedBy": "汪政",
      "addedByAccount": "24510",
      "addedByRole": "指导教师",
      "selectionStatus": "未选择"
    },
    {
      "projectId": "SITP2026016",
      "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
      "projectType": "创新训练项目",
      "name": "基于多智能体强化学习的路径规划与协同感知算法",
      "primaryDiscipline": "工学",
      "secondaryDiscipline": "计算机类",
      "college": "计算机科学与技术学院",
      "addedBy": "金博",
      "addedByAccount": "23125",
      "addedByRole": "指导教师",
      "selectionStatus": "未选择"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "total": 20,
    "totalPages": 1
  }
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

---

## 2. 根据项目ID获取项目详情

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/projects/{projectId}`

**参数：**
- `projectId`（路径参数，必填）：项目的唯一标识符
- `token`（必填）：用户认证令牌

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 从路径参数中提取 `projectId`
3. 调用 `ProjectService.getProjectById(projectId)` 方法
4. `ProjectService` 委托 `ProjectRepository.findById(projectId)` 从数据库查询指定项目
5. 如果项目不存在，返回 404 错误
6. 将 `Project` 实体转换为响应格式并返回

**请求示例：**
```json
GET /api/sitp/projects/SITP2024001?token=userToken12345
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取项目详情成功",
  "data": {
    "projectId": "SITP2026001",
    "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
    "projectType": "创新训练项目",
    "name": "基于多智能体强化学习的路径规划与协同感知算法",
    "primaryDiscipline": "工学",
    "secondaryDiscipline": "计算机类",
    "projectDuration": "一年期",
    "college": "计算机科学与技术学院",
    "teachers": [
      {
        "sequence": 1,
        "name": "金博",
        "account": "23125",
        "college": "计算机科学与技术学院",
        "role": "第一指导教师",
        "email": "bjin@tongji.edu.cn"
      }
    ],
    "teacherStrength": "机器学习、多智能体强化学习，大模型",
    "description": "本课题的目标是研究基于多智能体强化学习路径规划与协同感知算法、实现探索未知区域中智能体自主决策与环境感知的方法。构建如开放环境多无人设备的环境协同感知仿真环境，包括无人系统抽象、感知模型定义、协同感知技术设计等，实现多智能体强化学习的路径规划、多智能体近端策略优化等算法解决复杂环境下多无人设备的路径规划问题，建立基于多智能体的高鲁棒与高效率的分布式信息融合与协同感知算法，实现开放区域N-M个典型目标的自主追踪。",
    "studentRequirements": "已经完成机器学习、Python等基础课程，熟悉Issac、强化学习等。",
    "recruitmentRequirements": {
      "expectedMajor": "计算机科学与技术、人工智能、软件工程等相关专业",
      "maxStudentCount": 5,
      "skillRequirements": [
        "具有一定的网络和人工智能编程基础",
        "具有钻研精神，敢于勇闯技术高峰"
      ],
      "description": "项目招募学生要求：希望学生的专业为计算机科学与技术、人工智能、软件工程等相关专业；学生总数原则上不超过五个人；需要学生具备的技能包括：1. 具有一定的网络和人工智能编程基础。2. 具有钻研精神，敢于勇闯技术高峰。"
    },
    "addedBy": "金博",
    "addedByAccount": "23125",
    "addedByRole": "指导教师",
    "createTime": "2025-12-17T18:45:50"
  }
}
```

**错误响应示例（项目不存在）：**
```json
{
  "status": "error",
  "message": "项目不存在",
  "errorCode": "PROJECT_NOT_FOUND",
  "projectId": "SITP2024999"
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

---

## 3. 搜索项目

**请求类型：** `POST`

**REST API 端点：** `/api/sitp/projects/search`

**参数：**
- `token`（必填）：用户认证令牌
- `criteria`（请求体，必填）：搜索条件对象，包含以下可选字段：
  - `keyword`：关键词搜索（选题名称、项目描述等）
  - `batchNumber`：批次筛选（如："2026年第一批SITP导师发布课题（非定向）"）
  - `primaryDiscipline`：一级学科筛选（如："工学"）
  - `secondaryDiscipline`：二级学科筛选（如："计算机类"）
  - `college`：所属学院筛选
  - `projectType`：选题类型筛选（如："创新训练项目"、"创业训练项目"）
  - `page`：页码（默认1）
  - `pageSize`：每页数量（默认20）

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 解析请求体中的 `SearchCriteria` 对象
3. 调用 `ProjectService.searchProjects(criteria, userId)` 方法
4. `ProjectService` 根据搜索条件构建查询，委托 `ProjectRepository` 执行数据库查询
5. 查询该用户对每个项目的选择状态（已选择/未选择）
6. 应用分页逻辑，返回符合条件的项目列表
7. 将 `Project` 实体列表转换为列表响应格式（简化字段，包含选择状态）
8. 返回搜索结果，包含分页信息

**请求示例：**
```json
POST /api/sitp/projects/search
Content-Type: application/json

{
  "token": "userToken12345",
  "criteria": {
    "keyword": "多智能体",
    "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
    "primaryDiscipline": "工学",
    "secondaryDiscipline": "计算机类",
    "college": "计算机科学与技术学院",
    "projectType": "创新训练项目",
    "page": 1,
    "pageSize": 20
  }
}
```

**响应示例：**
```json
{
  "status": "success",
  "message": "搜索完成",
  "data": [
    {
      "projectId": "SITP2026016",
      "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
      "projectType": "创新训练项目",
      "name": "基于多智能体强化学习的路径规划与协同感知算法",
      "primaryDiscipline": "工学",
      "secondaryDiscipline": "计算机类",
      "college": "计算机科学与技术学院",
      "addedBy": "金博",
      "addedByAccount": "23125",
      "addedByRole": "指导教师",
      "selectionStatus": "未选择"
    },
    {
      "projectId": "SITP2026017",
      "batchNumber": "2026年第一批SITP导师发布课题（非定向）",
      "projectType": "创新训练项目",
      "name": "面向智能仓储的多智能体路径规划方法",
      "primaryDiscipline": "工学",
      "secondaryDiscipline": "计算机类",
      "college": "计算机科学与技术学院",
      "addedBy": "金博",
      "addedByAccount": "23125",
      "addedByRole": "指导教师",
      "selectionStatus": "未选择"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "total": 2,
    "totalPages": 1
  }
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

**错误响应示例（无效的搜索条件）：**
```json
{
  "status": "error",
  "message": "无效的搜索条件",
  "errorCode": "INVALID_CRITERIA",
  "details": "pageSize 不能超过 100"
}
```

---

## 4. 手动刷新项目数据

**请求类型：** `POST`

**REST API 端点：** `/api/sitp/projects/refresh`

**参数：**
- `token`（必填）：用户认证令牌（需要管理员权限）
- `force`（可选，布尔值）：是否强制刷新，即使距离上次刷新时间未到间隔

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 验证用户权限，确认用户具有管理员权限。如果权限不足，返回 403 错误
3. 检查是否允许刷新（如果 `force` 为 false，检查距离上次刷新时间是否满足最小间隔）
4. 调用 `ProjectService.refreshProjects()` 方法
5. `ProjectService` 调用 `SITPCrawler.crawlProjects()` 从 SITP 官网爬取最新项目数据
6. 将爬取到的项目数据转换为 `Project` 实体列表
7. 调用 `ProjectRepository.saveAll(projects)` 保存或更新项目数据到数据库
8. 返回刷新结果，包括更新的项目数量

**请求示例：**
```json
POST /api/sitp/projects/refresh
Content-Type: application/json

{
  "token": "adminToken12345",
  "force": false
}
```

**响应示例：**
```json
{
  "status": "success",
  "message": "项目数据刷新成功",
  "data": {
    "refreshTime": "2024-01-20T15:30:00",
    "totalCrawled": 25,
    "newProjects": 3,
    "updatedProjects": 22,
    "failedProjects": 0
  }
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

**错误响应示例（权限不足）：**
```json
{
  "status": "error",
  "message": "权限不足，需要管理员权限",
  "errorCode": "INSUFFICIENT_PERMISSIONS"
}
```

**错误响应示例（刷新间隔未到）：**
```json
{
  "status": "error",
  "message": "距离上次刷新时间过短，请稍后再试",
  "errorCode": "REFRESH_INTERVAL_NOT_MET",
  "nextAvailableTime": "2024-01-20T16:00:00"
}
```
---



---以下为ai相关接口

## 5. AI助手咨询请求

**请求类型：** `POST`

**REST API 端点：** `/api/sitp/ai-assistant/chat`

**参数：**
- `token`（必填）：用户认证令牌
- `promptType`（请求体，必填）：提示词类型，可选值：
  - `PROJECT_ANALYSIS`：项目匹配分析
  - `SKILL_ASSESSMENT`：技能评估
  - `FREE_QUESTION`：自由问答
- `userInput`（请求体，必填）：用户输入的咨询内容
- `projectId`（请求体，可选）：当前页面关联的项目ID，项目匹配分析和技能评估时必填

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `userId`。如果 `token` 无效或缺失，返回 401 错误
2. 验证请求参数，确保 `promptType` 和 `userInput` 有效
3. 调用 `AIAssistantService.processRequest(promptType, userInput, projectId, userId)` 方法
4. **步骤1：获取用户历史记录** - `AIAssistantService` 调用 `Conversation历史记录.loadHistory(userId)` 获取用户的历史对话记录
5. **步骤2：获取用户档案** - `AIAssistantService` 调用 `UserProfile接口.getUserProfile(userId)` 获取用户的课程背景、技能标签、项目经历等结构化数据
6. **步骤3：获取当前页面项目数据** - 如果提供了 `projectId`，`AIAssistantService` 调用 `ProjectInfo页面数据.fetchProjectInfo(projectId)` 获取目标 SITP 项目的详细描述、技能要求和导师信息
7. **步骤4：选择Prompt并构建最终输入** - `AIAssistantService` 根据 `promptType` 动态选择对应的 `AbstractPrompt` 子类（`ProjectAnalysisPrompt`、`UserSkillAssessmentPrompt` 或 `FreeQuestionPrompt`），调用 `selectPrompt(promptType, UserProfile, ProjectInfo, history)` 获取 `PromptInstance`，然后调用 `buildPrompt(userInput, UserProfile, ProjectInfo, history)` 构建最终的提示词文本
8. **步骤5：调用LLM生成响应** - `AIAssistantService` 调用 `LLMService.generateResponse(promptText)` 发送提示词至大语言模型，获取生成的响应内容
9. **步骤6：保存对话历史** - `AIAssistantService` 调用 `Conversation历史记录.saveConversation(userId, userInput, promptText, llmResponse)` 持久化完整的交互轨迹
10. **步骤7：返回结果给用户** - 将 LLM 生成的响应返回给用户，响应内容包含自然语言分析说明，以及结构化的匹配度评分、技能差距分析和学习路径建议（根据 `promptType` 不同而有所差异）

**请求示例：**
```json
POST /api/sitp/ai-assistant/chat
Content-Type: application/json

{
  "token": "userToken12345",
  "promptType": "PROJECT_ANALYSIS",
  "userInput": "我想了解这个项目是否适合我，以及我需要具备哪些技能？",
  "projectId": "SITP2026001"
}
```

**响应示例（项目匹配分析）：**
```json
{
  "status": "success",
  "message": "AI助手响应生成成功",
  "data": {
    "conversationId": "conv_20240120_123456",
    "response": {
      "text": "根据您的背景分析，该项目与您的匹配度为85%。您已具备Python编程和机器学习基础，这与项目要求高度匹配。建议您重点加强强化学习相关知识，特别是多智能体系统的理解。",
      "structuredData": {
        "matchScore": 0.85,
        "matchDimensions": [
          {
            "dimension": "技能匹配",
            "score": 0.90,
            "description": "您的Python和机器学习技能与项目需求高度匹配"
          },
          {
            "dimension": "专业背景",
            "score": 0.85,
            "description": "您的计算机专业背景与项目方向一致"
          },
          {
            "dimension": "项目经验",
            "score": 0.75,
            "description": "您有相关项目经验，但需要补充强化学习方面的实践"
          }
        ],
        "skillGaps": [
          {
            "skill": "强化学习",
            "gapLevel": "中等",
            "suggestion": "建议学习《强化学习基础》课程，并完成相关实践项目"
          },
          {
            "skill": "多智能体系统",
            "gapLevel": "较大",
            "suggestion": "建议阅读相关论文，了解多智能体协同感知的基本原理"
          }
        ],
        "learningPath": [
          {
            "step": 1,
            "action": "学习强化学习基础理论",
            "duration": "2-3周",
            "resources": ["《强化学习：原理与Python实现》", "Coursera相关课程"]
          },
          {
            "step": 2,
            "action": "完成多智能体系统实践项目",
            "duration": "3-4周",
            "resources": ["GitHub开源项目", "相关论文阅读"]
          }
        ]
      }
    },
    "timestamp": "2024-01-20T15:30:00"
  }
}
```

**响应示例（技能评估）：**
```json
{
  "status": "success",
  "message": "AI助手响应生成成功",
  "data": {
    "conversationId": "conv_20240120_123457",
    "response": {
      "text": "根据您的课程记录和项目经历，您的技能图谱如下：在编程能力方面表现优秀，Python和Java熟练度较高；在机器学习领域有良好基础，但深度学习实践经验需要加强；在系统设计方面有中等水平。",
      "structuredData": {
        "skillMap": [
          {
            "category": "编程能力",
            "skills": [
              {
                "name": "Python",
                "level": "高级",
                "score": 0.85,
                "evidence": ["已完成《Python程序设计》课程", "有3个相关项目经验"]
              },
              {
                "name": "Java",
                "level": "中级",
                "score": 0.70,
                "evidence": ["已完成《Java面向对象编程》课程"]
              }
            ]
          },
          {
            "category": "机器学习",
            "skills": [
              {
                "name": "机器学习基础",
                "level": "中级",
                "score": 0.75,
                "evidence": ["已完成《机器学习导论》课程"]
              },
              {
                "name": "深度学习",
                "level": "初级",
                "score": 0.50,
                "evidence": ["有1个相关项目经验"]
              }
            ]
          }
        ],
        "overallAssessment": "您的技术栈较为全面，建议在深度学习实践和系统架构设计方面继续提升。"
      }
    },
    "timestamp": "2024-01-20T15:31:00"
  }
}
```

**响应示例（自由问答）：**
```json
{
  "status": "success",
  "message": "AI助手响应生成成功",
  "data": {
    "conversationId": "conv_20240120_123458",
    "response": {
      "text": "SITP项目的申请流程通常包括：1. 浏览项目列表，选择感兴趣的项目；2. 联系指导教师，了解项目详情；3. 准备申请材料，包括个人简历、成绩单等；4. 提交申请，等待审核结果。整个流程一般需要1-2周时间。",
      "structuredData": null
    },
    "timestamp": "2024-01-20T15:32:00"
  }
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

**错误响应示例（参数无效）：**
```json
{
  "status": "error",
  "message": "参数验证失败",
  "errorCode": "INVALID_PARAMETERS",
  "details": "promptType为PROJECT_ANALYSIS或SKILL_ASSESSMENT时，projectId为必填项"
}
```

**错误响应示例（项目不存在）：**
```json
{
  "status": "error",
  "message": "项目不存在",
  "errorCode": "PROJECT_NOT_FOUND",
  "projectId": "SITP2024999"
}
```

**错误响应示例（LLM服务异常）：**
```json
{
  "status": "error",
  "message": "AI服务暂时不可用，请稍后重试",
  "errorCode": "LLM_SERVICE_UNAVAILABLE"
}
```

---

## 6. 获取用户对话历史

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/ai-assistant/history`

**参数：**
- `token`（必填）：用户认证令牌
- `page`（可选，整数）：页码，从1开始，默认为1
- `pageSize`（可选，整数）：每页数量，默认为20，最大为100
- `projectId`（可选，字符串）：筛选特定项目的对话历史

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `userId`。如果 `token` 无效或缺失，返回 401 错误
2. 解析分页参数：`page`（默认1）和 `pageSize`（默认20，最大100）
3. 调用 `AIAssistantService.getConversationHistory(userId, page, pageSize, projectId)` 方法
4. `AIAssistantService` 调用 `Conversation历史记录.loadHistory(userId, page, pageSize, projectId)` 从数据库分页查询对话历史
5. 返回对话历史列表，包含用户输入、AI响应、时间戳等信息

**请求示例：**
```json
GET /api/sitp/ai-assistant/history?token=userToken12345&page=1&pageSize=20&projectId=SITP2026001
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取对话历史成功",
  "data": [
    {
      "conversationId": "conv_20240120_123456",
      "projectId": "SITP2026001",
      "projectName": "基于多智能体强化学习的路径规划与协同感知算法",
      "promptType": "PROJECT_ANALYSIS",
      "userInput": "我想了解这个项目是否适合我，以及我需要具备哪些技能？",
      "aiResponse": {
        "text": "根据您的背景分析，该项目与您的匹配度为85%...",
        "structuredData": {
          "matchScore": 0.85
        }
      },
      "timestamp": "2024-01-20T15:30:00"
    },
    {
      "conversationId": "conv_20240120_123457",
      "projectId": "SITP2026001",
      "projectName": "基于多智能体强化学习的路径规划与协同感知算法",
      "promptType": "SKILL_ASSESSMENT",
      "userInput": "评估一下我的技能水平",
      "aiResponse": {
        "text": "根据您的课程记录和项目经历，您的技能图谱如下...",
        "structuredData": {
          "skillMap": []
        }
      },
      "timestamp": "2024-01-20T15:31:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "total": 15,
    "totalPages": 1
  }
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

---

## 7. 获取对话详情

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/ai-assistant/conversations/{conversationId}`

**参数：**
- `conversationId`（路径参数，必填）：对话的唯一标识符
- `token`（必填）：用户认证令牌

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `userId`。如果 `token` 无效或缺失，返回 401 错误
2. 从路径参数中提取 `conversationId`
3. 调用 `AIAssistantService.getConversationDetail(conversationId, userId)` 方法
4. `AIAssistantService` 调用 `Conversation历史记录.findById(conversationId)` 查询对话详情
5. 验证对话是否属于当前用户，如果不是，返回 403 错误
6. 返回完整的对话记录，包括用户输入、实际发送的提示词、模型原始响应和时间戳元数据

**请求示例：**
```json
GET /api/sitp/ai-assistant/conversations/conv_20240120_123456?token=userToken12345
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取对话详情成功",
  "data": {
    "conversationId": "conv_20240120_123456",
    "userId": "user12345",
    "projectId": "SITP2026001",
    "projectName": "基于多智能体强化学习的路径规划与协同感知算法",
    "promptType": "PROJECT_ANALYSIS",
    "userInput": "我想了解这个项目是否适合我，以及我需要具备哪些技能？",
    "promptText": "你是一位专业的科研项目匹配顾问。以下是用户信息和项目信息...\n[完整的提示词内容]",
    "llmResponse": {
      "text": "根据您的背景分析，该项目与您的匹配度为85%...",
      "structuredData": {
        "matchScore": 0.85,
        "matchDimensions": [],
        "skillGaps": [],
        "learningPath": []
      }
    },
    "timestamp": "2024-01-20T15:30:00",
    "createdAt": "2024-01-20T15:30:00",
    "updatedAt": "2024-01-20T15:30:00"
  }
}
```

**错误响应示例（对话不存在）：**
```json
{
  "status": "error",
  "message": "对话记录不存在",
  "errorCode": "CONVERSATION_NOT_FOUND",
  "conversationId": "conv_20240120_999999"
}
```

**错误响应示例（权限不足）：**
```json
{
  "status": "error",
  "message": "无权访问该对话记录",
  "errorCode": "INSUFFICIENT_PERMISSIONS"
}
```

---

## 8. 获取AI助手所需项目信息

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/projects/{projectId}/ai-assistant-info`

**参数：**
- `projectId`（路径参数，必填）：项目的唯一标识符
- `token`（必填）：用户认证令牌

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 从路径参数中提取 `projectId`
3. 调用 `ProjectService.getAIAssistantInfo(projectId)` 方法
4. `ProjectService` 从 `ProjectRepository` 获取指定项目
5. 对项目调用 `Project.toAIAssistantInfo()` 方法，将项目转换为 `AIAssistantInfo` 格式
6. 将转换后的信息返回，供 AI 助手服务使用

**请求示例：**
```json
GET /api/sitp/projects/SITP2026001/ai-assistant-info?token=userToken12345
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取AI助手信息成功",
  "data": {
    "projectId": "SITP2026001",
    "name": "基于多智能体强化学习的路径规划与协同感知算法",
    "primaryDiscipline": "工学",
    "secondaryDiscipline": "计算机类",
    "projectDuration": "一年期",
    "college": "计算机科学与技术学院",
    "teacher": "金博",
    "teacherStrength": "机器学习、多智能体强化学习，大模型",
    "description": "本课题的目标是研究基于多智能体强化学习路径规划与协同感知算法、实现探索未知区域中智能体自主决策与环境感知的方法。构建如开放环境多无人设备的环境协同感知仿真环境，包括无人系统抽象、感知模型定义、协同感知技术设计等，实现多智能体强化学习的路径规划、多智能体近端策略优化等算法解决复杂环境下多无人设备的路径规划问题，建立基于多智能体的高鲁棒与高效率的分布式信息融合与协同感知算法，实现开放区域N-M个典型目标的自主追踪。",
    "studentRequirements": "已经完成机器学习、Python等基础课程，熟悉Issac、强化学习等。",
    "projectType": "创新训练项目"
  }
}
```

**错误响应示例（token无效或缺失）：**
```json
{
  "status": "error",
  "message": "无效的认证令牌，请先登录",
  "errorCode": "INVALID_TOKEN"
}
```

**错误响应示例（项目不存在）：**
```json
{
  "status": "error",
  "message": "项目不存在",
  "errorCode": "PROJECT_NOT_FOUND",
  "projectId": "SITP2024999"
}
```


## 9. 获取项目静态特征（推荐算法接口）

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/projects/{projectId}/features/static`

**参数：**
- `projectId`（路径参数，必填）：项目的唯一标识符
- `token`（必填）：用户认证令牌（推荐服务使用服务间认证token）

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 从路径参数中提取 `projectId`
3. 调用 `ProjectService.getProjectStaticFeatures(projectId)` 方法
4. `ProjectService` 委托 `ProjectRepository.findById(projectId)` 从数据库查询指定项目
5. 如果项目不存在，返回 404 错误
6. 调用 `FeatureService.getStaticFeatures(project)` 获取项目的静态特征
7. `FeatureService` 从特征存储中获取或计算以下特征：
   - `projectDirection`：从项目的 `primaryDiscipline` 和 `secondaryDiscipline` 推导科研方向
   - `requiredSkills`：从项目的 `recruitmentRequirements.skillRequirements` 和 `studentRequirements` 提取技能标签列表
   - `skillEmbedding`：对技能标签进行Embedding向量化（离线计算）
   - `descriptionEmbedding`：对项目描述进行Embedding向量化（离线计算）
   - `metadata`：提取项目的元数据信息
8. 将静态特征数据转换为响应格式并返回

**请求示例：**
```json
GET /api/sitp/projects/SITP2026001/features/static?token=serviceToken12345
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取项目静态特征成功",
  "data": {
    "projectId": "SITP2026001",
    "projectDirection": "计算机类-机器学习",
    "requiredSkills": [
      "Python",
      "机器学习",
      "强化学习",
      "多智能体系统",
      "深度学习"
    ],
    "skillEmbedding": [0.123, -0.456, 0.789, 0.234, -0.567, 0.890, 0.345, -0.678, 0.901, 0.456],
    "descriptionEmbedding": [0.234, -0.567, 0.890, 0.345, -0.678, 0.901, 0.456, -0.789, 0.012, 0.567],
    "metadata": {
      "title": "基于多智能体强化学习的路径规划与协同感知算法",
      "description": "本课题的目标是研究基于多智能体强化学习路径规划与协同感知算法、实现探索未知区域中智能体自主决策与环境感知的方法。构建如开放环境多无人设备的环境协同感知仿真环境，包括无人系统抽象、感知模型定义、协同感知技术设计等，实现多智能体强化学习的路径规划、多智能体近端策略优化等算法解决复杂环境下多无人设备的路径规划问题，建立基于多智能体的高鲁棒与高效率的分布式信息融合与协同感知算法，实现开放区域N-M个典型目标的自主追踪。",
      "requiredMajor": ["计算机科学与技术", "人工智能", "软件工程"],
      "requiredGrade": []
    }
  }
}
```


## 10. 获取项目动态特征（推荐算法接口）

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/projects/{projectId}/features/dynamic`

**参数：**
- `projectId`（路径参数，必填）：项目的唯一标识符
- `token`（必填）：用户认证令牌（推荐服务使用服务间认证token）

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 从路径参数中提取 `projectId`
3. 调用 `ProjectService.getProjectDynamicFeatures(projectId)` 方法
4. `ProjectService` 委托 `ProjectRepository.findById(projectId)` 验证项目是否存在
5. 如果项目不存在，返回 404 错误
6. 调用 `FeatureService.getDynamicFeatures(projectId)` 获取项目的动态特征
7. `FeatureService` 从特征存储中获取定时/近线计算的动态特征：
   - `viewCount`：从访问日志统计项目的浏览量
   - `applicationCount`：统计项目的申请数量（选择该项目的用户数）
   - `popularityScore`：根据浏览量、申请数、时间衰减等因素计算的热度评分
   - `updatedAt`：特征最后更新时间
8. 将动态特征数据转换为响应格式并返回

**请求示例：**
```json
GET /api/sitp/projects/SITP2026001/features/dynamic?token=serviceToken12345
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取项目动态特征成功",
  "data": {
    "projectId": "SITP2026001",
    "viewCount": 156,
    "applicationCount": 23,
    "popularityScore": 0.85,
    "updatedAt": "2024-01-20T15:30:00"
  }
}
```


## 11. 获取热门项目池（推荐算法接口）

**请求类型：** `GET`

**REST API 端点：** `/api/sitp/projects/popular`

**参数：**
- `token`（必填）：用户认证令牌（推荐服务使用服务间认证token）
- `topN`（可选，整数）：返回数量，默认100，最大为500
- `primaryDiscipline`（可选，字符串）：一级学科筛选（如："工学"）
- `secondaryDiscipline`（可选，字符串）：二级学科筛选（如："计算机类"）

**服务器端处理步骤：**
1. 验证用户身份，验证 `token` 的有效性，获取对应的 `UserID`。如果 `token` 无效或缺失，返回 401 错误
2. 解析查询参数：`topN`（默认100，最大500）、`primaryDiscipline`（可选）、`secondaryDiscipline`（可选）
3. 调用 `ProjectService.getPopularProjects(topN, primaryDiscipline, secondaryDiscipline)` 方法
4. `ProjectService` 调用 `FeatureService.getPopularProjectPool(topN, primaryDiscipline, secondaryDiscipline)` 获取热门项目池
5. `FeatureService` 从特征存储中获取定时离线计算的热门/优质项目池：
   - 根据项目的 `popularityScore`、`viewCount`、`applicationCount` 等指标排序
   - 如果提供了学科筛选条件，按条件过滤
   - 返回前 `topN` 个项目的ID列表
   - 返回内容池的最后更新时间
6. 将热门项目池数据转换为响应格式并返回

**请求示例：**
```json
GET /api/sitp/projects/popular?token=serviceToken12345&topN=100&primaryDiscipline=工学&secondaryDiscipline=计算机类
```

**响应示例：**
```json
{
  "status": "success",
  "message": "获取热门项目池成功",
  "data": {
    "projectIds": [
      "SITP2026001",
      "SITP2026002",
      "SITP2026003",
      "SITP2026016",
      "SITP2026017"
    ],
    "updatedAt": "2024-01-20T15:30:00"
  }
}
```


---

## 数据模型说明

### Project 实体属性

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectId | String | 项目唯一标识符 |
| batchNumber | String | 所属批次 |
| projectType | String | 项目类型（创新训练项目/创业训练项目等） |
| name | String | 项目名称（选题名称） |
| primaryDiscipline | String | 主学科（所属一级学科） |
| secondaryDiscipline | String | 次学科（所属二级学科） |
| projectDuration | String | 项目持续时间（项目期限） |
| college | String | 所属学院 |
| teachers | Array<Teacher> | 指导教师列表（支持多个指导教师） |
| teacherStrength | String | 教师研究特长（指导教师指导本课题的特长与优势） |
| description | String | 项目描述（项目介绍） |
| studentRequirements | String | 学生要求（项目招募学生要求的简要描述） |
| recruitmentRequirements | RecruitmentRequirements | 项目招募学生要求（结构化数据，包含专业要求、学生人数、技能要求等） |
| addedBy | String | 添加人姓名 |
| addedByAccount | String | 添加人账号 |
| addedByRole | String | 添加人角色 |
| createTime | DateTime | 创建时间（添加时间） |
| selectionStatus | String | 选择状态（仅列表接口返回，可选值：已选择/未选择） |

### ProjectListInfo 结构（列表接口返回的简化结构）

列表接口返回的项目信息为简化版本，包含以下字段：

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectId | String | 项目唯一标识符 |
| batchNumber | String | 所属批次 |
| projectType | String | 选题类型 |
| name | String | 选题名称 |
| primaryDiscipline | String | 一级学科 |
| secondaryDiscipline | String | 二级学科 |
| college | String | 所属学院 |
| addedBy | String | 申报人姓名 |
| addedByAccount | String | 申报人账号 |
| addedByRole | String | 申报人角色 |
| selectionStatus | String | 选择状态（已选择/未选择） |

### Teacher 对象结构

| 属性名 | 类型 | 说明 |
|--------|------|------|
| sequence | Integer | 序号 |
| name | String | 教师姓名 |
| account | String | 教师账号 |
| college | String | 所属学院 |
| role | String | 角色（第一指导教师/第二指导教师等） |
| email | String | 电子邮箱 |

### RecruitmentRequirements 结构（项目招募学生要求）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| expectedMajor | String | 希望学生的专业（如："计算机科学与技术、人工智能、软件工程等相关专业"） |
| maxStudentCount | Integer | 学生总数上限（原则上不超过5个人） |
| skillRequirements | Array<String> | 需要学生具备的技能列表 |
| description | String | 完整的项目招募学生要求描述（包含专业、人数、技能等所有要求） |

### RecommendationInfo 结构

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectId | String | 项目唯一标识符 |
| name | String | 项目名称 |
| primaryDiscipline | String | 主学科 |
| secondaryDiscipline | String | 次学科 |
| teacher | String | 指导教师 |
| teacherStrength | String | 教师特长 |
| description | String | 项目描述 |
| studentRequirements | String | 学生要求 |

### AIAssistantInfo 结构

与 `Project` 实体类似，包含项目的基本信息，用于 AI 助手进行项目分析和问答。

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectId | String | 项目唯一标识符 |
| name | String | 项目名称 |
| primaryDiscipline | String | 主学科 |
| secondaryDiscipline | String | 次学科 |
| projectDuration | String | 项目持续时间 |
| college | String | 所属学院 |
| teacher | String | 指导教师 |
| teacherStrength | String | 教师研究特长 |
| description | String | 项目描述 |
| studentRequirements | String | 学生要求 |
| projectType | String | 项目类型 |

### PromptType 枚举

| 值 | 说明 |
|----|------|
| PROJECT_ANALYSIS | 项目匹配分析，将学生画像与项目需求进行多维度对比 |
| SKILL_ASSESSMENT | 技能评估，专注于学生能力图谱的构建 |
| FREE_QUESTION | 自由问答，支持开放式对话场景 |

### Conversation 结构（对话记录）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| conversationId | String | 对话唯一标识符 |
| userId | String | 用户ID |
| projectId | String | 关联的项目ID（可选） |
| projectName | String | 项目名称（可选） |
| promptType | PromptType | 提示词类型 |
| userInput | String | 用户原始输入 |
| promptText | String | 实际发送给LLM的完整提示词（仅详情接口返回） |
| llmResponse | AIResponse | LLM生成的响应内容 |
| timestamp | DateTime | 对话时间戳 |
| createdAt | DateTime | 记录创建时间 |
| updatedAt | DateTime | 记录更新时间 |

### AIResponse 结构（AI响应）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| text | String | 自然语言分析说明 |
| structuredData | Object | 结构化数据，根据promptType不同而有所差异，可能为null |

### ProjectAnalysisData 结构（项目匹配分析的结构化数据）

当 `promptType` 为 `PROJECT_ANALYSIS` 时，`structuredData` 包含以下字段：

| 属性名 | 类型 | 说明 |
|--------|------|------|
| matchScore | Float | 总体匹配度评分（0-1之间） |
| matchDimensions | Array<MatchDimension> | 多维度匹配分析 |
| skillGaps | Array<SkillGap> | 技能差距分析 |
| learningPath | Array<LearningStep> | 学习路径建议 |

### MatchDimension 结构（匹配维度）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| dimension | String | 维度名称（如：技能匹配、专业背景、项目经验等） |
| score | Float | 该维度的匹配度评分（0-1之间） |
| description | String | 该维度的匹配情况说明 |

### SkillGap 结构（技能差距）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| skill | String | 技能名称 |
| gapLevel | String | 差距等级（较小/中等/较大） |
| suggestion | String | 学习建议 |

### LearningStep 结构（学习步骤）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| step | Integer | 步骤序号 |
| action | String | 学习行动描述 |
| duration | String | 预计时长 |
| resources | Array<String> | 推荐学习资源 |

### SkillAssessmentData 结构（技能评估的结构化数据）

当 `promptType` 为 `SKILL_ASSESSMENT` 时，`structuredData` 包含以下字段：

| 属性名 | 类型 | 说明 |
|--------|------|------|
| skillMap | Array<SkillCategory> | 技能图谱，按类别组织 |
| overallAssessment | String | 整体评估说明 |

### SkillCategory 结构（技能类别）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| category | String | 技能类别名称（如：编程能力、机器学习等） |
| skills | Array<Skill> | 该类别下的技能列表 |

### Skill 结构（技能项）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| name | String | 技能名称 |
| level | String | 技能水平（初级/中级/高级） |
| score | Float | 技能评分（0-1之间） |
| evidence | Array<String> | 评估依据（如：已完成的课程、项目经验等） |

### ProjectStaticFeatures 结构（项目静态特征）

用于推荐算法的项目静态特征数据。

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectId | String | 项目唯一标识符 |
| projectDirection | String | 科研方向（从一级学科和二级学科推导） |
| requiredSkills | Array<String> | 所需技能标签列表（从招募要求中提取） |
| skillEmbedding | Array<Float> | 技能Embedding向量（离线计算） |
| descriptionEmbedding | Array<Float> | 项目描述Embedding向量（离线计算） |
| metadata | ProjectMetadata | 项目元数据信息 |

### ProjectMetadata 结构（项目元数据）

| 属性名 | 类型 | 说明 |
|--------|------|------|
| title | String | 项目名称（选题名称） |
| description | String | 项目描述（项目介绍） |
| requiredMajor | Array<String> | 专业要求列表 |
| requiredGrade | Array<String> | 年级要求列表（如为空则表示无年级限制） |

### ProjectDynamicFeatures 结构（项目动态特征）

用于推荐算法的项目动态特征数据。

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectId | String | 项目唯一标识符 |
| viewCount | Integer | 浏览量（从访问日志统计） |
| applicationCount | Integer | 申请数（选择该项目的用户数） |
| popularityScore | Float | 热度评分（0-1之间，定时/近线计算） |
| updatedAt | DateTime | 特征最后更新时间 |

### PopularProjectPool 结构（热门项目池）

用于推荐算法的热门项目池数据。

| 属性名 | 类型 | 说明 |
|--------|------|------|
| projectIds | Array<String> | 热门项目ID列表（按热度排序） |
| updatedAt | DateTime | 内容池最后更新时间 |

---

## 错误码说明

| 错误码 | HTTP状态码 | 说明 |
|--------|-----------|------|
| PROJECT_NOT_FOUND | 404 | 项目不存在 |
| INSUFFICIENT_PERMISSIONS | 403 | 权限不足 |
| REFRESH_INTERVAL_NOT_MET | 429 | 刷新间隔未到 |
| INVALID_TOKEN | 401 | 无效的认证令牌 |
| INVALID_CRITERIA | 400 | 无效的搜索条件 |
| INVALID_PARAMETERS | 400 | 参数验证失败 |
| CONVERSATION_NOT_FOUND | 404 | 对话记录不存在 |
| LLM_SERVICE_UNAVAILABLE | 503 | AI服务暂时不可用 |
| INTERNAL_ERROR | 500 | 服务器内部错误 |

---

## 注意事项

1. **认证要求：** 用户访问 SITP 模块的所有接口，必须先通过系统登录获取有效的 `token`，并在请求中携带该 `token`。所有接口的 `token` 参数均为**必填**，未提供有效 `token` 的请求将被拒绝，返回 401 未授权错误。
2. 刷新项目数据接口需要管理员权限，普通用户无法调用
3. 搜索接口支持分页，默认每页20条记录
4. 所有时间字段使用 ISO 8601 格式（YYYY-MM-DDTHH:mm:ss）
5. 项目数据通过定时爬虫自动更新，申报高峰期间每周更新一次，平常时期每月更新一次
6. 获取所有项目列表接口返回的是简化数据，仅包含列表展示所需的基本字段
7. 列表接口中的 `selectionStatus` 字段表示当前用户对该项目的选择状态（已选择/未选择）
8. **AI助手接口说明：**
   - AI助手咨询请求接口支持三种提示词类型：项目匹配分析（PROJECT_ANALYSIS）、技能评估（SKILL_ASSESSMENT）和自由问答（FREE_QUESTION）
   - 当 `promptType` 为 `PROJECT_ANALYSIS` 或 `SKILL_ASSESSMENT` 时，`projectId` 为必填项
   - AI助手会自动获取用户档案、项目信息和历史对话记录，构建上下文丰富的提示词
   - 所有对话记录都会被持久化保存，用户可以通过对话历史接口查看过往的咨询记录
   - AI响应包含自然语言说明和结构化数据，结构化数据根据不同的提示词类型而有所差异
   - 对话详情接口会返回完整的提示词内容，便于调试和优化
9. **推荐算法接口说明：**
   - 推荐算法接口（接口9-11）主要用于推荐模块获取项目的特征数据，支持服务间认证
   - 静态特征接口返回项目的技能标签、Embedding向量等离线计算的特征数据
   - 动态特征接口返回项目的浏览量、申请数、热度评分等定时/近线计算的特征数据
   - 热门项目池接口返回按热度排序的项目ID列表，支持按学科筛选
   - 推荐模块会在Redis中缓存特征数据（静态特征1小时TTL，动态特征30分钟TTL），减少API调用
   - 特征数据在项目发布/爬取时离线计算，或在用户行为发生后定时/近线计算

---

## 接口统计表

| API Interface | Method | Parameters | Description |
|---------------|--------|------------|-------------|
| `/api/sitp/projects` | GET | `token`、`page`、`pageSize` | 获取所有项目列表，支持分页，返回简化的项目信息列表 |
| `/api/sitp/projects/{projectId}` | GET | `projectId`、`token` | 根据项目ID获取项目详情，返回完整的项目信息包括招募要求等 |
| `/api/sitp/projects/search` | POST | `token`、`criteria` | 根据条件搜索项目，支持多维度筛选和分页 |
| `/api/sitp/projects/refresh` | POST | `token`、`force` | 手动刷新项目数据，从SITP官网爬取最新项目信息 |
| `/api/sitp/ai-assistant/chat` | POST | `token`、`promptType`、`userInput`、`projectId` | AI助手咨询请求，支持项目匹配分析、技能评估和自由问答三种类型 |
| `/api/sitp/ai-assistant/history` | GET | `token`、`page`、`pageSize`、`projectId` | 获取用户对话历史，支持分页和按项目筛选 |
| `/api/sitp/ai-assistant/conversations/{conversationId}` | GET | `conversationId`、`token` | 获取对话详情，返回完整的对话记录包括提示词和响应 |
| `/api/sitp/projects/{projectId}/ai-assistant-info` | GET | `projectId`、`token` | 获取AI助手所需项目信息，返回项目的基本信息供AI助手使用 |
| `/api/sitp/projects/{projectId}/features/static` | GET |projectId、token | 获取项目静态特征，返回技能标签、Embedding向量等特征数据（推荐算法接口） |
| `/api/sitp/projects/{projectId}/features/dynamic` | GET | `projectId`、`token` | 获取项目动态特征，返回浏览量、申请数、热度评分等特征数据（推荐算法接口） |
| `/api/sitp/projects/popular` | GET | `token`、`topN`、`primaryDiscipline`、`secondaryDiscipline` | 获取热门项目池，返回按热度排序的项目ID列表（推荐算法接口） |

