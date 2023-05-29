from __future__ import division

import argparse

import numpy as np
import pandas as pd
import pymysql
from pandas import DataFrame
from sklearn import preprocessing
from scipy.special import comb
from sklearn.metrics import adjusted_rand_score, f1_score, accuracy_score, precision_score, \
    precision_recall_fscore_support
from sklearn.datasets import *
import sys
import matplotlib.pyplot as plt
import matplotlib.colors as colors
from sklearn.metrics import davies_bouldin_score
import csv
import random
from time import time
from matplotlib.font_manager import FontProperties
import logging
import operator
# from AQI import calculate
import warnings
warnings.filterwarnings("ignore")

def loadData(filename):
    dataSet = []
    fileIn = open(filename, encoding='utf-8')
    fileIn.__next__()  # 跳过头文件
    for line in fileIn.readlines():
        lineArr = line.strip().split(',')
        dataSet.append([float(lineArr[4]),float(lineArr[5]),float(lineArr[6]), float(lineArr[7]), float(lineArr[8]), float(lineArr[9])])
    data = np.array(dataSet)
    return data

def loadData1(filename):
    dataSet = []
    fileIn = open(filename,encoding="utf-8")
    fileIn.__next__()  # 跳过头文件
    for line in fileIn.readlines():
        lineArr = line.strip().split(',')
        dataSet.append([str(lineArr[0]),float(lineArr[4]),float(lineArr[5]),float(lineArr[6]), float(lineArr[7]), float(lineArr[8]), float(lineArr[9])])
    data = np.array(dataSet)
    return data

# 数据预处理
def Data_preprocessing(data):
    data = data.tolist()
    # print(data)
    # 记录异常点下标
    flag = []
    for i in range(len(data)):
        for j in range(len(data[i])):
            if data[i][j] == 0:
                data[i][j] = 0.000001
            if data[i][j] < 0 or data[i][j] == None:
                flag.append(i)
                # data.remove(data[i])
                break
    for j in range(len(flag)):
        data.remove(data[flag[j]])
    # 返回处理后的数据集
    print(len(data))
    return data

def save(data_last, i,dataname):
    with open("results/"+dataname+"{}.csv".format(i), "w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        head = ['PM2_5 mg/m3','PM10 mg/m3','SO2 mg/m3','NO2 mg/m3','CO mg/m3','O3 mg/m3']
        writer.writerow(head)
        for i in range(len(data_last)):  # 前一个向量
            # 先写入columns_name
            writer.writerow(data_last[i])

def lesser_greater(a,b,isMax):
    if isMax == 0:
        return min(a,b)
    else:
        return max(a,b)

# x,y为数据点
# 相似返回相似度sim，不相似返回0
def sim(x,y,c):
    min = 1
    for i in range(len(x)): # 遍历属性
        sim_ = lesser_greater(x[i],y[i],0)/lesser_greater(x[i],y[i],1)
        if sim_ < c: # 小于阈值，不相似
            min = 0
            break
        elif sim_ < min:
            min = sim_
    return min


def EPC(data01, n, c, save_flag, dataname, data):
    k = -1
    centre = []
    label = np.zeros(n)
    # flag存放数据集是否被标记,长度为n
    flag = np.zeros(n)
    simset = np.zeros(n)
    for i in range(n):
        # flag[i] = 1,已被标记
        if flag[i] == 1:
            continue
        # xi未被标记，则选为中心点
        centre.append(data01[i])
        simset[i] =1
        flag[i] = 1
        k = k + 1
        label[i] = k
        for j in range(n):
            while data01[j] != centre[k]:
                # xj未被标记
                s = sim(data01[j], centre[k], c)
                if flag[j] == 0:
                    if s == 0: # xj和centre不相似
                        break
                    # xj属于第k个簇
                    label[j] = k
                    # 标记xj
                    flag[j] = 1
                    # 记录xj与现在所在簇簇中心的sim
                    simset[j] = s
                    break
                # xj被标记
                else:
                    if s >= simset[j]: # 与现簇中心相似度更大，将xj加入到现在的簇
                        label[j] == k
                        simset[j] = s
                        break
                    else:
                        break
    # print('centre1:',centre)
    print('len(centre1):',len(centre))
    # print('type(centre1):',type(centre))
    Updating(data01, n, label, centre,c)
    # print('label:',label)
    t = []
    if save_flag == 1:
        Cluster = [[] for i in range(k+1)]
        for i in range(k):
            for j in range(len(data01)):
                if i == label[j]:
                    Cluster[i].append(data[j])
            if len(Cluster[i]) > 10:
                save(Cluster[i], i, dataname)
                t.append(i)

            # save(Cluster[i], i)
    # print('t:', t)
    # print("label:", label)
    return label, t

# 更新簇中心点
def Updating(data01,n,label,centre,c):
    centrenew = []
    # 以簇中数据点均值作为新的簇中心点
    for i in range(len(centre)): # 遍历每个簇
        C = [] # 暂存每个簇中的数据
        for j in range(n):
            if label[j] == i:
                C.append(data01[j])
        if len(C) > 0:
            centrenew.append((np.mean(C,0)).tolist()) # 0:对列求均值
    # 以上求得新的簇中心
    simset = np.zeros(n)
    # print('type(centrenew):',type(centrenew))
    # print('centrenew:',centrenew)
    k = len(centrenew)
    # if any(centre != centrenew):
    if not(operator.eq(centre,centrenew)):
        for i in range(n):
            # print('centrenew:',centrenew)
            for j in range(len(centrenew)):

                if sim(data01[i],centrenew[j],c) > simset[i]:
                    simset[i] = sim(data01[i],centrenew[j],c)
                    label[i] = j
            if simset[i] == 0: #异常点
                label[i] = k+1
    # print('len(centrenew):',len(centrenew))
    if not(operator.eq(centre,centrenew)):
        Updating(data01,n,label,centrenew,c)

# 寻优，找到聚类较好的k值
def find_great(data_s,dataname,data):
    min = float('inf')
    flag = 0
    m = -1
    # c = [i/100.0 for i in range(1, 81)]
    c=[0.005]
    index = [[0]*2 for _ in range(len(c))]
    for i in c:
        m = m+1
        pre, k_ = EPC(data_s, len(data_s), i, 0, dataname, data)

        print(pre)
        DB = davies_bouldin_score(data_s, pre)
        index[m][0] = i
        index[m][1] = DB
        if DB < min:
            min = DB
            flag = i
    # print("index", index)
    return min, flag
# 全聚类
def cluster_all():
    # data_all = loadData("lanzhou.csv")
    # dataname_all = "百合公园_春"
    #
    # 接受数据样例
    k = 4  # 簇数
    area_id = 2309  # 地区id
    start_time = '2020-10-1'  # 开始时间
    end_time = '2020-12-1'  # 结束时间

    parser = argparse.ArgumentParser()
    parser.add_argument('-k', '--k', default='4', help=u'k')
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

    dataname_all='lanzhou'
    data_processed = Data_preprocessing(data1)
    # print(data_processed)
    DB_great, c_great = find_great(data_processed, dataname_all, data1)
    # print("DB_great_all:", DB_great)
    # print("c_great_all:", c_great)
    # label_pre, k = EPC(data_processed, len(data_processed), c_great, 1, dataname_all, data_all)
    # for i in k:
    #     path = "results/"+dataname_all+"{}.csv".format(i)
    #     print('path:', path)
    #     calculate(path, i, dataname_all)
#
# def cluster_part(data):
#     data_part = pd.read_csv("datasets/百合公园_春.csv", usecols=['PM2.5','PM10'])
#     dataname_part = "百合公园_春_part"
#     # print(data_part)
#     data_part = np.array(data_part)
#     data_part_processed = Data_preprocessing(data_part)
#     DB_great, c_great = find_great(data_part_processed, dataname_part, data)
#     print("DB_great_part:", DB_great)
#     print("c_great_part:", c_great)
#     label_pre, k = EPC(data_part_processed, len(data_part_processed), c_great, 0, dataname_part, data)
#     # for i in k:
#     #     path = "results/"+dataname_part+"{}.csv".format(i)
#     #     print('path:',path)
#     #     calculate(path,i,dataname_part)


if __name__ == "__main__":
    # data = loadData("lanzhou.csv")

    # data_time = loadData1("datasets/百合公园_春.csv")
    # 全聚类
    cluster_all()
    # 部分聚类
    # cluster_part(data)






