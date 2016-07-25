[TOC]

# 无敌坑爹打印机测试接口

该接口为移动组和Web前端共有。

## 格式

### request

```
{
    "action": string, // action名
    "data": JSON.stringify(object) // 要传入后台的参数，记住要先序列化
}
```

### response

```
{
    "data": {string}, // 后台返回值-字符串类型
    "status": [0|1], // 0表示成功，1表示失败
    "msg": {string} // 提示消息
}
```

## 接口

### 自动获取所有订单数据

该接口为自动获取正在打印,未打印的订单或打印完成的订单

- url: ['orsers/typing'|'orders/typed']  // [正在打印的订单路径|打印完的订单路径]
- type: 'GET'
- data: 'json'
- dataType: 'json'

#### request

```
{
    "user_id": {string}
}
```

#### response

以下的status的状态 110XX分别对应不一样的异常信息，该信息在前端那里判断并提示相应的异常信息，关于异常状态码和异常信息的对应状况由后台提供。

```
{ data:[
        {    // data 数组中包含多个订单数据
            "status":  {number},       // 100 ,打印完
                                       // 120 ,正在打印
                                       // 130 ,未打印
                                       // 110xx, 该数据格式为110[0-9]{2},分别对应不同的异常信息
            "order_id": {string},        // 订单编号
         },……
    ]
}
```

### 监控打印机状态

- url: 'printer'
- type: 'GET'
- data: 'json'
- dataType: 'json'

#### request

```
{
    user_id: {String} // 商家的id
}
```

#### response

```
{
    data: [
    {
        "printer_id": {String}
        "status": {number}      // 100: 健康
                                // 110: 异常
                                // 120：亚健康
        "message": {String}     // 失败信息
    }
]
}
```

#### 其他

目前还有**设置图片**，**设置二维码**接口没写。
