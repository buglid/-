import csv
import numpy as np
import pandas as pd


from sklearn import preprocessing
from sklearn.preprocessing import MinMaxScaler, StandardScaler
import pymysql


def Normalization():
    df = pd.read_csv("iris.csv",usecols=["a","b","c","d"])  # 读文件返回DataFrame对象
    df = pd.DataFrame(df)
    data_arr = np.array(df)
    # print(data_arr)
    #
    # Z - score标准化
    r2 = StandardScaler().fit_transform(data_arr)  # 标准化处理
    print(r2)

    print("------------我是一条分界线------------------")

    #(0,1)标准化调用函数计算
    min_max_normalizer = preprocessing.MinMaxScaler(feature_range=(0, 1))
    # feature_range设置最大最小变换值，默认（0,1）
    scaled_data = min_max_normalizer.fit_transform(df)
    # 将数据缩放(映射)到设置固定区间
    price_frame_normalized = pd.DataFrame(scaled_data)
    # 将变换后的数据转换为dataframe对象
    print(price_frame_normalized)

    print("------------我是一条分界线------------------")

    #(0,1)标准化使用公式计算
    data = df.describe()  # 计算相关指标
    data = np.array(data)
    for i in range(len(data_arr)):
        for j in range(data_arr.shape[1]):
            #计算公式newValue = (oldValue - min)/(max - min)
            data_arr[i][j] = (data_arr[i][j] - data[3][j])/(data[7][j] - data[3][j])
    print(data_arr)


if __name__ == '__main__':
    # path = 'E:/project/python/pythonProject2/test1/lanzhou.csv'
    # datasets = genfromtxt(open(path, 'r'), dtype=float, delimiter=',',
    #                       skip_header=1)
    # # data = np.delete(data, 0, axis=0)  # 删除行
    # data = np.delete(datasets, [0], axis=1)  # 删除列
    # print(data)

    # 接受数据样例
    # k = 4   #簇数
    # area_id = 2309   #地区id
    # start_time = '2020-10-1'   #开始时间
    # end_time = '2020-12-1'     #结束时间

    parser = argparse.ArgumentParser()
    parser.add_argument('-k', '--k', default='3', help=u'k')
    parser.add_argument('-a', '--area_id', default='2309', help=u'area_id')
    parser.add_argument('-s', '--start_time', default='2020-10-1', help=u'start_time')
    parser.add_argument('-e', '--end_time', default='2022-12-1', help=u'end_time')
    args = parser.parse_args()

    k = int(args.k)
    area_id = int(args.area_id)
    start_time = args.start_time
    end_time = args.end_time

    db = pymysql.connect(host='localhost',
                         port=3306,
                         user='root',
                         password='root',
                         database='ry',
                         charset='utf8')

    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = db.cursor()

    # 查询语句
    try:
        cursor = db.cursor()
        sql = f"select * from pollution where area_id={area_id} and date>='{start_time}' and date<='{end_time}'"
        cursor.execute(sql)
        db.commit()
        data2 = cursor.fetchall()
        df = DataFrame(data2)
        df.head()
        datasets = np.array(df)
        data1 = np.delete(datasets, [0, 1, 2, 9, 10, 11], axis=1)  # 删除列

    except Exception:
        print("查询失败")
    db.close()