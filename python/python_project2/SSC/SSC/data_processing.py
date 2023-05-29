
import numpy as np
from numpy import genfromtxt
from sklearn import preprocessing

def mean_nan(dataset):
    for i in range(dataset.shape[1]):  #遍历每一列
        temp = dataset[:,i]  # 当前的列数
        nan_num = np.count_nonzero(temp!=temp)  #nan数量
        if nan_num!=0:
            temp_nan = temp[temp==temp]
            meannan = np.round(np.mean(temp_nan),2)
            temp[temp!=temp] = meannan
    return dataset
def data_pro(data, index=0):



    data = np.array(data)
    data = data.astype(np.float64)
    data = mean_nan(data)
    print("数据标准化中...")
    min_max_scaler = preprocessing.MinMaxScaler()
    X_minMax = min_max_scaler.fit_transform(data)
    print("数据标准化完成！")

    return X_minMax
