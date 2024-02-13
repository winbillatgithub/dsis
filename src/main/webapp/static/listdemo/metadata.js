/**
 * 元数据
 */
var metadata = [
{
	"student": {
		"name":"学生",
		"code":"student",
		"properties": 
			[{"colCode": "id","des":"id"},
			 {"colCode": "name","des":"名称"}
			]
		
	}
},
{
	"Class": {
		"name":"班级",
		"code":"Class",
		"properties": {
			"id": {
				"des":"id"
			},
			"classNum": {
				"des":"班级号"
			}
		}
	}
},
{
	"Teacher": {
		"name":"老师",
		"code":"Teacher",
		"properties": {
			"id": {
				"des":"id"
			},
			"name": {
				"des":"姓名"
			},
			"classNum": {
				"des":"班级"
			},
			"title": {
				"des":"职务"
			},
			"phone": {
				"des":"电话"
			},
			"email": {
				"des":"邮箱"
			}
		}
	}
}
];