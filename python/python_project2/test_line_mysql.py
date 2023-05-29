import pymysql
from pandas import DataFrame
import numpy as np

db = pymysql.connect(host='localhost',
                     user='root',
                     password='root',
                     database='world',
                     charset='utf8')

# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()

# 查询语句
try:
    cursor = db.cursor()
    sql = "select * from pollution"
    cursor.execute(sql)
    db.commit()
    data = cursor.fetchall()


    df = DataFrame(data, columns=['id', 'area_id', 'Date', 'PM2.5', 'PM10','SO2','NO2','CO','O3','enter_date','user_id','remarks'])
    df.head()
    datasets = np.array(df)
    data1 = np.delete(datasets, [0,1,2,9,10,11], axis=1)  # 删除列
    print(data1)


except Exception:
    print("查询失败")

db.close()