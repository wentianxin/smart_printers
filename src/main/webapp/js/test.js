var ORDER_TYPING = 'http://101/order/typing';  // 正在打印的订单路径接口
var PRINTER_TYPING = 'http://101/printf';// 打印机状态接口
var ORDER_TYPED = 'http://101/order/typed'; // 打印完的订单路径接口

Mock.mock(ORDER_TYPING,{
   // data: [
   //      {    // data 数组中包含多个订单数据
   //          "orderStatus":  0,             // 100 ,打印完
   //                                      // 120 ,正在打印
   //                                      // 130 ,未打印
   //                                      // 110xx, 该数据格式为110[0-9]{2},分别对应不同的异常信息
   //          "id": "4000138",         // 订单编号
   //      },{
   //          "orderStatus": 0,
   //          "id": "4000139"
   //      },{
   //          "orderStatus": 0,
   //          "id": "4000140",
   //      }
   //  ]
});

Mock.mock(PRINTER_TYPING,{
//     data: [
//     {
//         "id": '1号打印机',
//         "printerStatus": 1,
//     },{
//         "id": '2号打印机',
//         "printerStatus": 12,
//     },{
//         "id": '3号打印机',
//         "printerStatus": 6 ,
//     }
// ]
});

Mock.mock(ORDER_TYPED,{
   data: [
        {    // data 数组中包含多个订单数据
            "orderStatus":  0,          // 100 ,打印完
                                        // 120 ,正在打印
                                        // 130 ,未打印
                                        // 110xx, 该数据格式为110[0-9]{2},分别对应不同的异常信息
            "id": "4000129",         // 订单编号
        },{
            "orderStatus": 0,
            "id": "4000130"
        },{
            "orderStatus": 0,
            "id": "4000131"
        },{
            "orderStatus": 4,
            "id": "4000132"
        },{
            "orderStatus": 5,
            "id": "4000133"
        },{
            "orderStatus": 5,
            "id": "4000134"
        },{
            "orderStatus": 4,
            "id": "4000135"
        }
    ]
});
