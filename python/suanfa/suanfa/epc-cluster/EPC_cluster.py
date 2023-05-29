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
import seaborn as sns
import palettable
from matplotlib.pyplot import savefig
from sklearn.metrics import silhouette_score, calinski_harabasz_score, davies_bouldin_score
import csv
import random
from time import time
from matplotlib.font_manager import FontProperties
import logging
import operator
sys.path.append("...")
from AQI import calculate

import warnings
warnings.filterwarnings("ignore")

from gmssl.sm4 import CryptSM4, SM4_ENCRYPT, SM4_DECRYPT
import binascii
from heapq import heappush, heappop
from collections import OrderedDict
import base64

class SM4:
    """
    国密sm4加解密
    """

    def __init__(self):
        self.crypt_sm4 = CryptSM4()

    def str_to_hexStr(self, hex_str):
        """
        字符串转hex
        :param hex_str: 字符串
        :return: hex
        """
        hex_data = hex_str.encode('utf-8')
        str_bin = binascii.unhexlify(hex_data)
        return str_bin.decode('utf-8')

    def encrypt(self, encrypt_key, value):
        """
        国密sm4加密
        :param encrypt_key: sm4加密key
        :param value: 待加密的字符串
        :return: sm4加密后的hex值
        """
        crypt_sm4 = self.crypt_sm4
        crypt_sm4.set_key(encrypt_key.encode(), SM4_ENCRYPT)
        date_str = str(value)
        encrypt_value = crypt_sm4.crypt_ecb(date_str.encode())  # bytes类型
        # return encrypt_value.hex()
        return base64.b64encode(encrypt_value)

    def decrypt(self, decrypt_key, encrypt_value):
        """
        国密sm4解密
        :param decrypt_key:sm4加密key
        :param encrypt_value: 待解密的hex值
        :return: 原字符串
        """
        crypt_sm4 = self.crypt_sm4
        crypt_sm4.set_key(decrypt_key.encode(), SM4_DECRYPT)
        # decrypt_value = crypt_sm4.crypt_ecb(bytes.fromhex(encrypt_value))  # bytes类型
        decrypt_value = crypt_sm4.crypt_ecb(base64.b64decode(encrypt_value))
        return self.str_to_hexStr(decrypt_value.hex())

def loadData(filename):
    dataSet = []
    fileIn = open(filename, encoding='utf-8')
    fileIn.__next__()  # 跳过头文件
    for line in fileIn.readlines():
        lineArr = line.strip().split(',')
        # dataSet.append([float(lineArr[3]), float(lineArr[4]), float(lineArr[5]), float(lineArr[6]), float(lineArr[7]), float(lineArr[8])])
        #
        dataSet.append([float(lineArr[1]), float(lineArr[2]), float(lineArr[3]), float(lineArr[4]), float(lineArr[5]),
                        float(lineArr[6])])
    data = np.array(dataSet)
    return data


# 数据预处理
def Data_preprocessing(data):
    data = data.tolist()
    data = [[float(num) for num in list] for list in data]
    print('参与处理数据')
    print(data);
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
    # print(data)
    # 返回处理后的数据集
    return data



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
        sim_ = lesser_greater(x[i], y[i], 0)/lesser_greater(x[i], y[i], 1)
        if sim_ < c: # 小于阈值，不相似
            min = 0
            break
        elif sim_ <= min:
            min = sim_
    return min


def EPC(data01, n, c, save_flag, data):
    k = -1
    centre = []
    label = np.zeros((n,),dtype= int)

    # flag存放数据集是否被标记,长度为n
    flag = np.zeros(n)
    simset = np.zeros(n)

    for i in range(n):
        # flag[i] = 1,已被标记
        if flag[i] == 1:
            continue # 进入下次循环
        # xi未被标记，则选为中心点
        centre.append(data01[i])
        simset[i] =1
        flag[i] = 1
        k = k + 1
        label[i] = k
        for j in range(n):
            if data01[j] == centre[k]:
                simset[j] = 1
                flag[j] = 1
                label[j] = k
                continue  # 进入下次循环
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

    Updating(data01, n, label, centre, c)
    t = []
    if save_flag == 1:
        Cluster = [[] for i in range(k+1)]
        for i in range(k):
            for j in range(len(data01)):
                if i == label[j]:
                    Cluster[i].append(data[j])
            if len(Cluster[i]) > 10:
                t.append(i)

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
            centrenew.append((np.mean(C, 0)).tolist()) # 0:对列求均值
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

                if sim(data01[i], centrenew[j], c) > simset[i]:
                    simset[i] = sim(data01[i], centrenew[j], c)
                    label[i] = j
            if simset[i] == 0: #异常点
                label[i] = k+1
    # print('len(centrenew):',len(centrenew))
    if not(operator.eq(centre, centrenew)):
        Updating(data01, n, label, centrenew, c)


# 寻优，找到聚类较好的阈值
def find_great_all(data_s,data):
    maxsi = -1
    maxch = -1
    mindb = 1
    a_ = []
    b_ = []
    c_ = []
    k = []
    a_.append('SC')
    b_.append('CH')
    c_.append('DB')
    k.append('簇个数')
    c = [i / 100.0 for i in range(3, 100)]
    # print(c)
    pre, k_ = EPC(data_s, len(data_s), 0.3, 1, data)
    # def EPC(data01, n, c, save_flag, data):
    print('result',pre)


def find_great_part(data_s,dataname,data):
    maxsi = -1
    maxch = -1
    mindb = 1
    a_ = []
    b_ = []
    c_ = []
    k = []
    a_.append('SC')
    b_.append('CH')
    c_.append('DB')
    k.append('簇个数')


    c = [i / 100.0 for i in range(15, 20)]
    for i in c:
        pre, k_ = EPC(data_s, len(data_s), i, 1, dataname, data)
        print(pre)
        si = round(silhouette_score(data_s, pre), 2)
        CH = round(calinski_harabasz_score(data_s, pre), 2)
        DB = round(davies_bouldin_score(data_s, pre), 2)
        a_.append(si)
        b_.append(CH)
        c_.append(DB)
        k.append(len(k_))
        if si >= maxsi:
            maxsi = si
            flag1 = i
        if CH >= maxch:
            maxch = CH
            flag2 = i
        if DB <= mindb:
            mindb = DB
            flag3 = i
    if flag1 == flag2 and flag1 == flag2 and flag1 == flag2:
        flag = flag1
    elif flag1 == flag2:
        flag = flag1
    elif flag1 == flag3:
        flag = flag1
    elif flag2 == flag3:
        flag = flag2
    else:
        flag = flag3
    return a_, b_, c_, c, k, flag
# 上面两个是分开阈值范围的寻优



if __name__ == "__main__":


    # 接受数据样例
    # k = 4  # 簇数
    # area_id = 2309  # 地区id
    # start_time = '2019-10-1'  # 开始时间
    # end_time = '2020-12-1'  # 结束时间

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

    key = "Da&^*9wHFOMfs2Y8"
    SM4 = SM4()
    db = pymysql.connect(host='localhost',
                         port=3306,
                         user='root',
                         password='root',
                         database='ruoyi',
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

    for i in range(len(data1)):
        for j in range(6):
            data1[i][j] = SM4.decrypt(key, data1[i][j])
    print(data1)

    # print(dataname_all)
    data_processed = Data_preprocessing(data1)
    # print(data_processed)
    find_great_all(data_processed, data1)

