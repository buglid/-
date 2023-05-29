import csv
import numpy as np
from itertools import islice
import os

SO2 = [[0, 0, ], [50, 150], [100, 500], [150, 650], [200, 800], [300, 1600], [400, 2100], [500, 2620]]
NO2 = [[0, 0], [50, 100], [100, 200], [150, 700], [200, 1200], [300, 2340], [400, 3090], [500, 3840]]
O3 = [[0, 0], [50, 160], [100, 200], [150, 300], [200, 400], [300, 800], [400, 1100], [500, 1200]]
CO = [[0, 0], [50, 5], [100, 10], [150, 35], [200, 60], [300, 90], [400, 120], [500, 150]]
PM_10 = [[0, 0], [50, 50], [100, 150], [150, 250], [200, 350], [300, 420], [400, 500], [500, 5000]]
PM_25 = [[0, 0], [50, 30], [100, 75], [150, 115], [200, 150], [300, 250], [400, 350], [500, 800]]


# 计算SO2的AQI
def cal_SO2(so2):
    data = np.array(so2)
    SO2_AQI = {}
    for i in range(len(data)):
        for j in range(7):
            if float(data[i]) > SO2[j][1] and float(data[i]) <= SO2[j + 1][1]:
                SO2_AQI[i] = (SO2[j + 1][0] - SO2[j][0]) / (SO2[j + 1][1] - SO2[j][1]) * (float(data[i]) - SO2[j][1]) + \
                             SO2[j][0]
            if float(data[i]) == 0:
                SO2_AQI[i] = data[i]
        # if data[i + 1] == '':
        #     break
    return SO2_AQI


# 计算NO2的AQI
def cal_NO2(no2):
    data = np.array(no2)
    NO2_AQI = {}
    for i in range(len(data)):
        for j in range(7):
            if float(data[i]) > NO2[j][1] and float(data[i]) <= NO2[j + 1][1]:
                NO2_AQI[i] = (NO2[j + 1][0] - NO2[j][0]) / (NO2[j + 1][1] - NO2[j][1]) * (float(data[i]) - NO2[j][1]) + \
                             NO2[j][0]
            if float(data[i]) == 0:
                NO2_AQI[i] = data[i]
        # if data[i + 1] == '':
        #     break
    return NO2_AQI


# 计算O3的AQI
def cal_O3(o3):
    data = np.array(o3)
    O3_AQI = {}
    for i in range(len(data)):
        for j in range(7):
            if float(data[i]) > O3[j][1] and float(data[i]) <= O3[j + 1][1]:
                O3_AQI[i] = (O3[j + 1][0] - O3[j][0]) / (O3[j + 1][1] - O3[j][1]) * (float(data[i]) - O3[j][1]) + \
                            O3[j][0]
            if float(data[i]) == 0:
                O3_AQI[i] = data[i]
        # 如果数据集最后又空行，直接跳出循环
        # if data[i + 1] == '':
        #     break
    return O3_AQI


# 计算CO的AQI
def cal_CO(co):
    data = np.array(co)
    CO_AQI = {}
    for i in range(len(data)):
        for j in range(7):
            if float(data[i]) > CO[j][1] and float(data[i]) <= CO[j + 1][1]:
                CO_AQI[i] = (CO[j + 1][0] - CO[j][0]) / (CO[j + 1][1] - CO[j][1]) * (float(data[i]) - CO[j][1]) + \
                            CO[j][0]
            if float(data[i]) == 0:
                CO_AQI[i] = data[i]
        # if data[i + 1] == '':
        #     break
    return CO_AQI


# 计算PM_10的AQI
def cal_PM_10(pm_10):
    data = np.array(pm_10)
    PM_10_AQI = {}
    # k = 0
    for i in range(len(data)):
        # print(data[i])
        for j in range(7):
            if float(data[i]) > PM_10[j][1] and float(data[i]) <= PM_10[j + 1][1]:
                # print(PM_10[j][1])
                PM_10_AQI[i] = (PM_10[j + 1][0] - PM_10[j][0]) / (PM_10[j + 1][1] - PM_10[j][1]) * (
                        float(data[i]) - PM_10[j][1]) + PM_10[j][0]
            if float(data[i]) == 0:
                PM_10_AQI[i] = data[i]
                # print(k,data[i])
                # k += 1
        # if data[i + 1] == '':
        #     break
    # print('if', k)
    return PM_10_AQI


# 计算PM_25的AQI
def cal_PM_25(pm_25):
    data = np.array(pm_25)
    # print(len(data))
    PM_25_AQI = {}
    k = 0
    for i in range(len(data)):
        for j in range(7):
            if float(data[i]) > PM_25[j][1] and float(data[i]) <= PM_25[j + 1][1]:
                # print(k)
                PM_25_AQI[i] = (PM_25[j + 1][0] - PM_25[j][0]) / (PM_25[j + 1][1] - PM_25[j][1]) * (
                        float(data[i]) - PM_25[j][1]) + PM_25[j][0]
                k += 1
            if float(data[i]) == 0:
                PM_25_AQI[i] = data[i]
        # if data[i + 1] == '':
        #     break
    # print('2',PM_25_AQI)
    # print(len(PM_25_AQI))
    return PM_25_AQI

def loadData(filename):
    dataSet = []
    fileIn = open(filename)
    fileIn.__next__()  # 跳过头文件
    for line in fileIn.readlines():
        lineArr = line.strip().split(',')
        dataSet.append([float(lineArr[0]),float(lineArr[1]),float(lineArr[2]), float(lineArr[3]), float(lineArr[4]), float(lineArr[5])])
    data = np.array(dataSet)
    return data

def save_(data_1, i,dataname):
    with open("results/"+dataname+"{}.csv".format(i), "w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        head = ['PM2.5', 'PM10', 'SO2', 'NO2', 'CO', 'O3', 'AQI', 'primary']
        writer.writerow(head)
        for i in range(len(data_1)):  # 前一个向量
            # 先写入columns_name
            writer.writerow(data_1[i])

def calculate(path,k,dataname):
    #批处理文件
    data = loadData(path)
    # 将各污染物的浓度值加载进来
    data_PM_25 = [x[0] for x in data]
    PM_25_IAQI = list(cal_PM_25(data_PM_25).values())

    data_PM_10 = [x[1] for x in data]
    PM_10_IAQI = list(cal_PM_10(data_PM_10).values())

    data_SO2 = [x[2]for x in data]
    SO2_IAQI = list(cal_SO2(data_SO2).values())

    data_NO2 = [x[3] for x in data]
    NO2_IAQI = list(cal_NO2(data_NO2).values())

    data_CO = [x[4] for x in data]
    CO_IAQI = list(cal_CO(data_CO).values())

    data_O3 = [x[5] for x in data]
    O3_IAQI = list(cal_O3(data_O3).values())

    data_ = data.tolist()
    # data1记录个污染物的IAQI
    data1 = [[] for i in range(len(data_))]

    # 遍历每一污染物IAQI计算AQI
    for r in range(len(data_)):
        # for s in range(len(data_[r])):
        data1[r].append(PM_25_IAQI[r])
        # print("PM_10_IAQI[r]:",PM_10_IAQI[r])
        data1[r].append(PM_10_IAQI[r])
        data1[r].append(SO2_IAQI[r])
        data1[r].append(NO2_IAQI[r])
        data1[r].append(CO_IAQI[r])
        data1[r].append(O3_IAQI[r])

    for i in range(len(data1)): # 计算每个数据点的AQI
        max = 0
        flag = 0
        for j in range(len(data1[i])):
            if data1[i][j] > max:
                max = data1[i][j]
                flag = j
        data_[i].append(round(max,2))
        data_[i].append(flag)

    save_(data_, k,dataname)


if __name__ == '__main__':
    calculate("results/百合公园_春4.csv",4,"百合公园_春")
