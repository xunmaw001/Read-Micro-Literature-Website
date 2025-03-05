const menu = {
    list() {
        return [
    {
        "backMenu":[
            {
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "删除",
                            "修改"
                        ],
                        "menu":"文学信息类型管理",
                        "menuJump":"列表",
                        "tableName":"dictionaryXiaoshuo"
                    }
                ],
                "menu":"基础数据管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"文学信息管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuo"
                    }
                    ,
                    {
                        "buttons":[
                            "查看",
                            "修改",
                            "删除"
                        ],
                        "menu":"文学信息留言管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoLiuyan"
                    }
                    ,
                    {
                        "buttons":[
                            "查看",
                            "删除"
                        ],
                        "menu":"书架管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoCollection"
                    }
                    ,
                    {
                        "buttons":[
                            "订单",
                            "查看",
                            "删除"
                        ],
                        "menu":"文学信息购买管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoOrder"
                    }
                ],
                "menu":"文学信息管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"章节管理",
                        "menuJump":"列表",
                        "tableName":"zhangjie"
                    }
                ],
                "menu":"章节管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"用户管理",
                        "menuJump":"列表",
                        "tableName":"yonghu"
                    }
                ],
                "menu":"用户管理"
            },
			{
			    "child":[
			        {
			            "buttons":[
			                "查看",
			                "新增",
			                "删除",
			                "修改"
			            ],
			            "menu":"论坛管理",
			            "menuJump":"列表",
			            "tableName":"forum"
			        }
			    ],
			    "menu":"论坛管理"
			}
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"阅读记录管理",
                        "menuJump":"列表",
                        "tableName":"yuedujilu"
                    }
                ],
                "menu":"阅读记录管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"作者管理",
                        "menuJump":"列表",
                        "tableName":"zuozhe"
                    }
                ],
                "menu":"作者管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"轮播图管理",
                        "menuJump":"列表",
                        "tableName":"config"
                    }
                ],
                "menu":"轮播图信息"
            }
        ],
        "frontMenu":[],
        "hasBackLogin":"是",
        "hasBackRegister":"否",
        "hasFrontLogin":"否",
        "hasFrontRegister":"否",
        "roleName":"管理员",
        "tableName":"users"
    }
	,
	{
        "backMenu":[
            {
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"文学信息管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuo"
                    }
                    ,
                    {
                        "buttons":[
                            "查看",
                            "修改"
                        ],
                        "menu":"文学信息留言管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoLiuyan"
                    }
                    ,
                    {
                        "buttons":[
                            "查看"
                        ],
                        "menu":"文学信息购买管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoOrder"
                    }
                ],
                "menu":"文学信息管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看",
                            "新增",
                            "修改",
                            "删除"
                        ],
                        "menu":"章节管理",
                        "menuJump":"列表",
                        "tableName":"zhangjie"
                    }
                ],
                "menu":"章节管理"
            },
			{
			    "child":[
			        {
			            "buttons":[
			                "查看",
			                "新增"
			            ],
			            "menu":"论坛管理",
			            "menuJump":"列表",
			            "tableName":"forum"
			        }
			    ],
			    "menu":"论坛管理"
			}
        ],
        "frontMenu":[],
        "hasBackLogin":"是",
        "hasBackRegister":"否",
        "hasFrontLogin":"否",
        "hasFrontRegister":"否",
        "roleName":"作者",
        "tableName":"zuozhe"
    }
	,
	{
        "backMenu":[
            {
                "child":[
                    {
                        "buttons":[
                            "查看"
                        ],
                        "menu":"文学信息管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuo"
                    }
                    ,
                    {
                        "buttons":[
                            "查看"
                        ],
                        "menu":"文学信息留言管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoLiuyan"
                    }
                    ,
                    {
                        "buttons":[
                            "订单",
                            "查看",
                            "删除"
                        ],
                        "menu":"文学信息购买管理",
                        "menuJump":"列表",
                        "tableName":"xiaoshuoOrder"
                    }
                ],
                "menu":"文学信息管理"
            }
            ,{
                "child":[
                    {
                        "buttons":[
                            "查看"
                        ],
                        "menu":"阅读记录管理",
                        "menuJump":"列表",
                        "tableName":"yuedujilu"
                    }
                ],
                "menu":"阅读记录管理"
            },
			{
			    "child":[
			        {
			            "buttons":[
			                "查看",
			                "新增"
			            ],
			            "menu":"论坛管理",
			            "menuJump":"列表",
			            "tableName":"forum"
			        }
			    ],
			    "menu":"论坛管理"
			}
        ],
        "frontMenu":[],
        "hasBackLogin":"是",
        "hasBackRegister":"否",
        "hasFrontLogin":"否",
        "hasFrontRegister":"否",
        "roleName":"用户",
        "tableName":"yonghu"
    }
]
    }
}
export default menu;
