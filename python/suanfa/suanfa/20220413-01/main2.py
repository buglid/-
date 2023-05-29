#%%
import argparse

import pandas as pd
#%%
import numpy as np
import pandas as pd
import pymysql
from pandas import DataFrame
from sklearn.cluster import KMeans, DBSCAN, AgglomerativeClustering
from sklearn.datasets import make_blobs

import pandas as pd
from sklearn.cluster import KMeans, DBSCAN, AgglomerativeClustering
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import silhouette_score
from sklearn.decomposition import PCA


if __name__ == '__main__':
    # path = 'E:/project/python/pythonProject2/test1/lanzhou.csv'
    # datasets = genfromtxt(open(path, 'r'), dtype=float, delimiter=',',
    #                       skip_header=1)
    # # data = np.delete(data, 0, axis=0)  # 删除行
    # data = np.delete(datasets, [0], axis=1)  # 删除列
    # print(data)

    # 接受数据样例
    k = 4   #簇数
    area_id = 2309   #地区id
    start_time = '2020-10-1'   #开始时间
    end_time = '2020-12-1'     #结束时间

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
    #
    db = pymysql.connect(host='localhost',
                         port=3306,
                         user='root',
                         password='123456',
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
    # print("ceshi")

    # 归一化处理
    scaler = MinMaxScaler()
    scaled_data = scaler.fit_transform(data1)

    # KMeans 聚类
    kmeans = KMeans(k)
    kmeans.fit(scaled_data)
    kmeans_labels = kmeans.labels_

    # 将聚类结果存入新列
    # data['KMeans_labels'] = kmeans_labels
    # data.to_excel('output.xlsx', index=False)

    print('result', kmeans_labels)
