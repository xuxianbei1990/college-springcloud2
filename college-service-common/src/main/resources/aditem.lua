--数据响应类型json
ngx.header.content_type="applicaiton/json;charset=utf8"
--redis库依赖
local redis = require("resty.redis")
local cjson = require("cjson")

--获取id参数(type)
local id = ngx.req.get_uri_args()["id"]
--key组装
local key = "ad-items-skus::"..id
--创建连接对象
local red = redis:new()
--设置超时时间
red:set_timeout(2000)
--设置服务器连接信息
red:connect("10.228.81.233", 16379)
red:select(5)
--查询指定key的数据
local result = red:get(key)

--关闭redis
red:close()

if result == nil or result == null or result == ngx.null then
    return true
else
    ngx.say(tostring(result))
end
