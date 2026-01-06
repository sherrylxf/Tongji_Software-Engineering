#### 4.1.4 特征数据API设计

根据特征工程计算时机，推荐模块需要通过HTTP API从个人中心模块和科研/竞赛模块获取已计算好的特征数据。以下为各模块需要提供的API接口：

**（1）个人中心模块API**

个人中心模块负责计算并存储用户静态特征和动态特征，为推荐模块提供以下接口：

**API 1：获取用户静态特征**
- **接口路径**：`GET /api/personal-center/users/{userId}/features/static`
- **请求参数**：
  - `userId`（路径参数）：用户ID
- **响应体**：
```json
{
  "userId": "string",
  "major": "string",           // 专业
  "grade": "string",            // 年级
  "skillTags": ["string"],      // 技能标签列表（从知识图谱推导）
  "knowledgeGraphSkills": ["string"]  // 知识图谱技能集合
}
```
- **说明**：返回用户注册/更新时离线计算的静态特征，包括专业、年级、技能标签等基础画像信息

**API 2：获取用户动态特征**
- **接口路径**：`GET /api/personal-center/users/{userId}/features/dynamic`
- **请求参数**：
  - `userId`（路径参数）：用户ID
- **响应体**：
```json
{
  "userId": "string",
  "competitionHistory": [      // 竞赛经历
    {
      "competitionId": "string",
      "competitionType": "string",
      "participatedAt": "datetime"
    }
  ],
  "researchBackground": [      // 科研背景
    {
      "projectId": "string",
      "projectDirection": "string",
      "participatedAt": "datetime"
    }
  ],
  "researchPreferences": {     // 科研偏好（基于历史行为统计）
    "directions": ["string"],   // 偏好的科研方向列表
    "preferenceWeights": {      // 各方向偏好权重
      "direction1": 0.5,
      "direction2": 0.3
    }
  }
}
```
- **说明**：返回用户行为发生后离线/近线计算的动态特征，反映用户历史行为与兴趣演化

**（2）科研/竞赛模块API**

科研/竞赛模块负责计算并存储内容静态特征、动态特征和热门内容池，为推荐模块提供以下接口：

**API 3：获取内容静态特征（竞赛组队）**
- **接口路径**：`GET /api/competition/teams/{teamId}/features/static`
- **请求参数**：
  - `teamId`（路径参数）：队伍ID
- **响应体**：
```json
{
  "teamId": "string",
  "competitionType": "string",      // 竞赛类型
  "requiredSkills": ["string"],      // 所需技能标签
  "skillEmbedding": [0.1, 0.2, ...], // 技能Embedding向量
  "metadata": {
    "title": "string",
    "description": "string",
    "requiredMajor": ["string"],     // 专业要求
    "requiredGrade": ["string"]      // 年级要求
  }
}
```
- **说明**：返回队伍发布/爬取时离线计算的静态特征，包括技能标签、Embedding向量、元数据等

**API 4：获取内容静态特征（科研项目）**
- **接口路径**：`GET /api/research/projects/{projectId}/features/static`
- **请求参数**：
  - `projectId`（路径参数）：项目ID
- **响应体**：
```json
{
  "projectId": "string",
  "projectDirection": "string",      // 科研方向
  "requiredSkills": ["string"],      // 所需技能标签
  "skillEmbedding": [0.1, 0.2, ...], // 技能Embedding向量
  "descriptionEmbedding": [0.1, 0.2, ...], // 项目描述Embedding向量
  "metadata": {
    "title": "string",
    "description": "string",
    "requiredMajor": ["string"],     // 专业要求
    "requiredGrade": ["string"]       // 年级要求
  }
}
```
- **说明**：返回项目发布/爬取时离线计算的静态特征，包括技能标签、Embedding向量、元数据等

**API 5：获取内容动态特征（竞赛组队）**
- **接口路径**：`GET /api/competition/teams/{teamId}/features/dynamic`
- **请求参数**：
  - `teamId`（路径参数）：队伍ID
- **响应体**：
```json
{
  "teamId": "string",
  "viewCount": 0,              // 浏览量
  "applicationCount": 0,       // 申请数
  "popularityScore": 0.0,      // 热度评分（定时/近线计算）
  "updatedAt": "datetime"      // 特征更新时间
}
```
- **说明**：返回定时/近线计算的内容动态特征，衡量内容质量与实时趋势

**API 6：获取内容动态特征（科研项目）**
- **接口路径**：`GET /api/research/projects/{projectId}/features/dynamic`
- **请求参数**：
  - `projectId`（路径参数）：项目ID
- **响应体**：
```json
{
  "projectId": "string",
  "viewCount": 0,              // 浏览量
  "applicationCount": 0,       // 申请数
  "popularityScore": 0.0,      // 热度评分（定时/近线计算）
  "updatedAt": "datetime"      // 特征更新时间
}
```
- **说明**：返回定时/近线计算的内容动态特征，衡量内容质量与实时趋势

**API 7：获取热门/优质内容池（竞赛组队）**
- **接口路径**：`GET /api/competition/teams/popular`
- **请求参数**：
  - `topN`（查询参数，可选）：返回数量，默认100
  - `competitionType`（查询参数，可选）：竞赛类型筛选
- **响应体**：
```json
{
  "teamIds": ["string"],        // 热门队伍ID列表
  "updatedAt": "datetime"       // 内容池更新时间
}
```
- **说明**：返回定时离线计算的热门/优质内容池，用于加速召回，保证基础推荐质量

**API 8：获取热门/优质内容池（科研项目）**
- **接口路径**：`GET /api/research/projects/popular`
- **请求参数**：
  - `topN`（查询参数，可选）：返回数量，默认100
  - `direction`（查询参数，可选）：科研方向筛选
- **响应体**：
```json
{
  "projectIds": ["string"],     // 热门项目ID列表
  "updatedAt": "datetime"       // 内容池更新时间
}
```
- **说明**：返回定时离线计算的热门/优质内容池，用于加速召回，保证基础推荐质量

**（3）API调用说明**

- **调用方**：推荐模块（Python微服务）通过HTTP客户端调用上述API
- **认证方式**：通过API网关进行身份验证，推荐服务使用服务间认证token
- **缓存策略**：推荐模块在Redis中缓存特征数据（用户特征30分钟TTL，内容特征1小时TTL），减少API调用
- **批量获取**：支持批量获取多个用户/内容的特征（如`GET /api/personal-center/users/features/static?userIds=id1,id2,id3`），提升性能
- **错误处理**：API调用失败时，推荐模块降级为直接从PostgreSQL读取特征数据（备选