import argparse

import numpy as np
import matplotlib.pyplot as plt

# 计算数据点两两之间的距离
# import pandas as pd
import pymysql
from numpy import genfromtxt
from pandas import DataFrame

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


def getDistanceMatrix(datas):
    N, D = np.shape(datas)
    dists = np.zeros([N, N])
    print(datas)
    datas = [[float(num) for num in list] for list in datas]
    datas = [[int(num) for num in list] for list in datas]
    datas = np.array(datas)
    print(datas)
    for i in range(N):
        for j in range(N):
            vi = datas[i, :]
            vj = datas[j, :]
            dists[i, j] = np.sqrt(np.dot((vi - vj), (vi - vj)))
    return dists


# 找到密度计算的阈值dc
# 要求平均每个点周围距离小于dc的点的数目占总点数的1%-2%
def select_dc(dists):
    '''算法1'''
    N = np.shape(dists)[0]
    tt = np.reshape(dists, N * N)
    percent = 2.0
    position = int(N * (N - 1) * percent / 100)
    dc = np.sort(tt)[position + N]

    ''' 算法 2 '''
    # N = np.shape(dists)[0]
    # max_dis = np.max(dists)
    # min_dis = np.min(dists)
    # dc = (max_dis + min_dis) / 2

    # while True:
    # n_neighs = np.where(dists<dc)[0].shape[0]-N
    # rate = n_neighs/(N*(N-1))

    # if rate>=0.01 and rate<=0.02:
    # break
    # if rate<0.01:
    # min_dis = dc
    # else:
    # max_dis = dc

    # dc = (max_dis + min_dis) / 2
    # if max_dis - min_dis < 0.0001:
    # break
    return dc


# 计算每个点的局部密度
def get_density(dists, dc, method=None):
    N = np.shape(dists)[0]
    rho = np.zeros(N)

    for i in range(N):
        if method == None:
            rho[i] = np.where(dists[i, :] < dc)[0].shape[0] - 1
        else:
            rho[i] = np.sum(np.exp(-(dists[i, :] / dc) ** 2)) - 1
    return rho


# 计算每个数据点的密度距离
# 即对每个点，找到密度比它大的所有点
# 再在这些点中找到距离其最近的点的距离
def get_deltas(dists, rho):
    N = np.shape(dists)[0]
    deltas = np.zeros(N)
    nearest_neiber = np.zeros(N)
    # 将密度从大到小排序
    index_rho = np.argsort(-rho)
    for i, index in enumerate(index_rho):
        # 对于密度最大的点
        if i == 0:
            continue

        # 对于其他的点
        # 找到密度比其大的点的序号
        index_higher_rho = index_rho[:i]
        # 获取这些点距离当前点的距离,并找最小值
        deltas[index] = np.min(dists[index, index_higher_rho])

        # 保存最近邻点的编号
        index_nn = np.argmin(dists[index, index_higher_rho])
        nearest_neiber[index] = index_higher_rho[index_nn].astype(int)

    deltas[index_rho[0]] = np.max(deltas)
    return deltas, nearest_neiber


# 通过阈值选取 rho与delta都大的点
# 作为聚类中心
def find_centers_auto(rho, deltas):
    rho_threshold = (np.min(rho) + np.max(rho)) / 2
    delta_threshold = (np.min(deltas) + np.max(deltas)) / 2
    N = np.shape(rho)[0]

    centers = []
    for i in range(N):
        if rho[i] >= rho_threshold and deltas[i] > delta_threshold:
            centers.append(i)
    return np.array(centers)


# 选取 rho与delta乘积较大的点作为
# 聚类中心
def find_centers_K(rho, deltas, K):
    rho_delta = rho * deltas
    centers = np.argsort(-rho_delta)
    return centers[:K]


def cluster_PD(rho, centers, nearest_neiber):
    K = np.shape(centers)[0]
    if K == 0:
        print("can not find centers")
        return

    N = np.shape(rho)[0]
    labs = -1 * np.ones(N).astype(int)

    # 首先对几个聚类中进行标号
    for i, center in enumerate(centers):
        labs[center] = i

    # 将密度从大到小排序
    index_rho = np.argsort(-rho)
    for i, index in enumerate(index_rho):
        # 从密度大的点进行标号
        if labs[index] == -1:
            # 如果没有被标记过
            # 那么聚类标号与距离其最近且密度比其大
            # 的点的标号相同
            labs[index] = labs[int(nearest_neiber[index])]
    return labs


def draw_decision(rho, deltas, name="0_decision.jpg"):
    plt.cla()
    for i in range(np.shape(deltas)[0]):
        plt.scatter(rho[i], deltas[i], s=16., color=(0, 0, 0))
        plt.annotate(str(i), xy=(rho[i], deltas[i]), xytext=(rho[i], deltas[i]))
        plt.xlabel("rho")
        plt.ylabel("deltas")
    # plt.show()
    plt.savefig(name)


def draw_cluster(datas, labs, centers, dic_colors, name="0_cluster.jpg"):
    plt.cla()
    K = np.shape(centers)[0]

    for k in range(K):
        sub_index = np.where(labs == k)
        sub_datas = datas[sub_index]
        # 画数据点
        plt.scatter(sub_datas[:, 0], sub_datas[:, 1], s=16., color=dic_colors[k])
        # 画聚类中心
        plt.scatter(datas[centers[k], 0], datas[centers[k], 1], color="k", marker="+", s=200.)
    plt.savefig(name)


def scatter_picture(data, labs):
    colors = ["r", "b", "g", "c", "m", "y", "k", "aqua", "tomato", "lawngreen", "turquoise", "saddlebrown", "violet",
              "deeppink", "grey"]
    for i, lab in enumerate(labs):
        # if lab == -1:
        #     plt.scatter(data[i,0],data[i,1],marker='^',c='indigo',s=110)
        # else:
        plt.scatter(data[i, 0], data[i, 1], s=2, color=colors[lab])
    plt.show()


def read_data(filename):
    # datasets = genfromtxt(open('C:/Users/yyyyz/Desktop/data/cluster/' + filename, 'r'), dtype=float, delimiter=',',
    #                       skip_header=1)
    datasets = genfromtxt(open( filename, 'r'), dtype=float, delimiter=',',
                          skip_header=1)
    y = datasets[:, 0]
    x = np.delete(datasets, 0, axis=1)
    return x, y


if __name__ == "__main__":
    # 接受数据样例
    # k = 4  # 簇数
    # area_id = 2309  # 地区id
    # start_time = '2020-10-1'  # 开始时间
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

        # print(datasets)
        data1 = np.delete(datasets, [0, 1, 2, 9, 10, 11], axis=1)  # 删除列

    except Exception:
        print("查询失败")
    db.close()

    for i in range(len(data1)):
        for j in range(6):
            data1[i][j] = SM4.decrypt(key,data1[i][j])
    # print(data1)

    dists = getDistanceMatrix(data1)

    # 计算dc
    dc = select_dc(dists)
    # print("dc", dc)
    # 计算局部密度
    rho = get_density(dists, dc, method="Gaussion")

    deltas, nearest_neiber = get_deltas(dists, rho)


    centers = find_centers_K(rho, deltas, 3)


    labs = cluster_PD(rho, centers, nearest_neiber)
    print('result', labs)


