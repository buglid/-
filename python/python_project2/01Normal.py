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

if __name__=="__main__":
    Normalization()