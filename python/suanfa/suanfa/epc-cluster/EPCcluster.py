import os
import sklearn
import numpy as np
import pandas as pd
import operator
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from sklearn import preprocessing
from sklearn.model_selection import train_test_split

def load_data(path):
    data = pd.read_csv(path,header=1)
    data.columns= ['PM2.5','PM10','CO','SO2','NO2','O3_8h']
    return data

def analysis_data(data):
    print('分析数据省略')
    # print("data shape:",data.shape)
    # print("data head 5:",data.head(5))
    # print("data info:",data.info())
    # print("data describle:",data.describe())

def deal_data(data):
    #不考虑label列
    dataS = data[['PM2.5','PM10','CO','SO2','NO2','O3_8h']]
    #将元素为0的置为1e-6
    dataS.replace(0,1e-6,inplace=True)
    #移除X_ij小于0的异常值
    dataS = dataS[~((dataS['PM2.5'] < 0) &
                    (dataS['PM10'] < 0) &
                    (dataS['CO'] < 0) &
                    (dataS['SO2'] < 0) &
                    (dataS['NO2'] < 0) &
                    (dataS['O3_8h'] < 0))]
    # print("deal_data dataS:",dataS)
    return dataS

def similiarty(data1,data2):
    dataConcat = pd.concat([data1,data2],axis=1)
    dataConcat = dataConcat.T
    #选取指定几列（含特征的几列）
    dataConcat = dataConcat[['PM2.5','PM10','CO','SO2','NO2','O3_8h']]
    # print(dataConcat)
    dataConcat = pd.DataFrame(dataConcat)
    columns = dataConcat.columns
    # print(columns)
    # print(dataConcat)
    result = dataConcat.min(0) / dataConcat.max(0)
    # print(result)
    sim = min(result)
    return sim

def lesser_greater(centre_kf,x_jf):
    dataConcat = pd.concat([centre_kf, x_jf], axis=1)
    dataConcat = dataConcat.T
    # 选取指定几列（含特征的几列）
    dataConcat = dataConcat[['PM2.5', 'PM10', 'CO', 'SO2', 'NO2', 'O3_8h']]
    # print(dataConcat)
    dataConcat = pd.DataFrame(dataConcat)
    columns = dataConcat.columns
    # print(columns)
    # print(dataConcat)
    result = dataConcat.min(0) / dataConcat.max(0)
    # print(result)
    sim = max(result)
    return sim

def model_one_epoch(data,lenColumn,yiTa,k,clusterCentre=0):
    for i in range(lenColumn):
        daCi = data.iloc[i,:]
        # print(daC['labeled'])
        #簇分类的开始条件为(int(daCi['labeled']) != -1)；但是在第一次分类结束后，可能所有数据都已经被分类
        #如果不加(isinstance(clusterCentre,int))条件，则下一次簇中心重新分配时，所有原始数据已经被标记
        #将不再继续运行下面代码，即无法继续更新簇中心。
        if (int(daCi['labeled']) != -1) and (isinstance(clusterCentre,int)):
            continue
        #标记已经划分过簇中心的原始数据
        data.loc[i,'labeled'] = 1
        #如果当前簇中心未被分类，那么新增簇分类，否则从已经分类的簇中心进行调整，不再新增簇分类。（初始化簇中心为每个数据的下标，即单独是一类）
        if data.loc[i,'ORcenter'] == i:
            k += 1
            data.loc[i,'ORcenter'] = k
        #如果当前数据已经被分类过，则加载更新的簇中心来调整原始数据，否则以当前数据为簇中心（初始化条件）
        if (not isinstance(clusterCentre,int)):
            clusterCentre = pd.DataFrame(clusterCentre)
            clusterCentre.index = [i for i in range(len(clusterCentre))]
            # print(clusterCentre)
            index = clusterCentre.loc[clusterCentre['ORcenter'] == int(daCi['ORcenter'])].index[0]
            # print("clusterCentre:",clusterCentre,"index:",index)
            clusterCentre_k = clusterCentre.iloc[int(index),:]
            # print("clusterCentre_k:",clusterCentre_k)
        else:
            clusterCentre_k = daCi
        #遍历每个数据
        for j in range(lenColumn):
            #当当前原始簇中心与所比较的簇中心不相同时，执行以下操作，（i！=j）表示不比较同一个元素
            while (int(data.loc[j,'ORcenter']) != int(clusterCentre_k['ORcenter'])) and (i != j):
                daCj = data.iloc[j, :]
                #如果未被标记，则计算相似度并划分簇类别
                if int(daCj['labeled']) == -1:
                    lg = lesser_greater(clusterCentre_k, daCj)
                    #如果当前数据与待比较簇中心相似度过低，则跳出循环
                    if lg < yiTa:
                        #centrek and xj is dissimilar
                        # print("centre {} and xj is dissimilar".format(k))
                        break
                    else:
                        #add the xj to the clusterk
                        #assign the label of clusterk to xj
                        data.loc[j,'labeled'] = 1
                        data.loc[j,'ORcenter'] = k
                        # print("add the xj to the cluster {},lg:{}".format(k,lg))
                else:
                    #如果待比较元素已经被划分簇中心，则与当前待比较簇中心进行相似度计算，根据相似度最高原则，重新划分族中心
                    # print(j,daCj)
                    if (daCj['ORcenter'] != j) and (similiarty(clusterCentre_k,daCj) >= similiarty(data.iloc[int(daCj['ORcenter']),:],daCj)) :
                        #remove xj from the original cluster
                        #add xj to clusterk
                        data.loc[j,'ORcenter'] = clusterCentre_k['ORcenter']
                        # print(similiarty(clusterCentre_k,daCj),similiarty(data.iloc[[daCj['ORcenter']],:],daCj))
                        # print("remove x{} from the original cluster,ORcenter:{},data new center:{}".format(j,data.loc[j,'ORcenter'],clusterCentre_k['ORcenter']))
                    else:
                        #如果待比较元素与原始簇中心相似度大于当前所比较簇中心的相似度，则跳出循环。
                        break
                # print("i:{},j:{},k:{},daCj labeled:{},data j cluster:{},clusterCentre_k:{}".format(i, j,k, data.loc[j,'labeled'], data.loc[j,'ORcenter'],clusterCentre_k['ORcenter']))
        # print("i:{}".format(i))

    return data,k


def epcCluster_model(dataOR):
    data = dataOR.copy()
    #shape[0]矩阵行数，即数据点数
    lenColumn = data.shape[0]
    lenRow = data.shape[1]
    # print(lenColumn,lenRow)
    #标记当前X_i 是否被标记，初始化为-1表示均未被标记
    data['labeled'] = -1
    # #划分簇类别，初始化 每个点是一个簇
    data['ORcenter'] = list(range(lenColumn))
    #聚类中心个数统计
    k = -1
    # 簇间相似度定义
    yiTa = 0.8
    #最终的簇
    clusterOld = [-2 for i in range(lenColumn)]
    clusterNew = [-1 for i in range(lenColumn)]
    clusterCentre = 0
    #外层循环运行统计
    total_epoch = 0
    #判定结束条件：簇中心不再变化（operator.eq(clusterNew,clusterOld):），；(-1 in clusterNew)代表初始化判定
    while (-1 in clusterNew):
        if operator.eq(clusterNew,clusterOld):
            break
        total_epoch += 1
        #迭代一次的聚类结果
        data,k = model_one_epoch(data, lenColumn, yiTa, k,clusterCentre)

        #采用均值重新计算聚类中心
        print("采用均值重新计算聚类中心\n")
        # print(data_clusterOnce)
        # print(k)
        clusterOld = data['ORcenter']
        clusterCentre = data.groupby(['ORcenter']).mean()
        clusterCentre['ORcenter'] = list(sorted(set(data['ORcenter'])))
        # print("clusterCentre   cc:",clusterCentre)
        #根据调整后的簇中心计算一次，判断原始簇中心和新簇中心是否变化。
        k2 = k
        data_clusterOnce2,k2 = model_one_epoch(data, lenColumn, yiTa, k2, clusterCentre)
        clusterNew = data_clusterOnce2['ORcenter']
        # print(clusterNew )
        print(set(clusterNew))
        # break

    print("fininsh train, total epoch is:",total_epoch)



if __name__ == '__main__':
    path = r'datasets/百合公园.csv'
    #读取数据
    data = load_data(path)
    #数据分析
    analysis_data(data)
    #数据处理
    dataS = deal_data(data)
    #模型训练和预测
    epcCluster_model(dataS)

