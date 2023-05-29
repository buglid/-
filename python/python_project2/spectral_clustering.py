import argparse

import numpy as np
import matplotlib.pyplot as plt
import pymysql
from pandas import DataFrame
from sklearn.cluster import KMeans
from sklearn.datasets import make_blobs
from sklearn.metrics.pairwise import pairwise_distances

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

def find_p(X, start=1, end=10):

    min_size=[]
    number_of_clusters=[]
    for i in range(start,end+1):
        min_size.append(i)   
        number_of_clusters.append(KMeans(n_clusters=i).fit(X).inertia_)
    _, ax=plt.subplots()
    
    # ax.set(ylabel='Inertia', xlabel='Number of clusters', title='The elbow method')
    # plt.xticks(np.arange(start,end, 1))
    # plt.plot(min_size,number_of_clusters)
    # plt.show()

def get_Landmarks(X, p, method="random"):
    if method=="random":
        N = len(X)
        perm= np.random.permutation(np.arange(N))
        print(perm)
        landmarks = X[perm[:p],:]
        return landmarks
    else:
        kmeans_model=KMeans(n_clusters=p).fit(X)
        return kmeans_model.cluster_centers_


def gaussian_kernel(dist_mat, bandwidth):
    return np.exp(-dist_mat / (2*bandwidth**2))

def compose_Sparse_ZHat_Matrix(X, landmarks, bandwidth, r):
    dist_mat=pairwise_distances(X,landmarks)
    sim_mat=gaussian_kernel(dist_mat, bandwidth)
    
    Zhat = np.zeros(sim_mat.shape)

    for i in range(Zhat.shape[0]):
        #may need j.sort.selectperm
        top_Landmarks_indices = np.argsort(-sim_mat[i,:])[:r]
        top_Landmarks_coefs = sim_mat[i,top_Landmarks_indices]
        top_Landmarks_coefs /= np.sum(top_Landmarks_coefs)
        Zhat[i, top_Landmarks_indices] = top_Landmarks_coefs

    diagm=np.sum(Zhat, axis=0)**(-1/2)
    return diagm*Zhat

def LSC_Clustering(X, n_clusters, n_landmarks, method, non_zero_landmark_weights, bandwidth):
    landmarks = get_Landmarks(X, n_landmarks, method)
    Zhat = compose_Sparse_ZHat_Matrix(X, landmarks, bandwidth, non_zero_landmark_weights)
    svd_result = np.linalg.svd(Zhat, full_matrices=False)[0]
    clustering_result = KMeans(n_clusters=n_clusters).fit(svd_result)
    return clustering_result

if __name__ == '__main__':

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

    # # 接受数据样例
    # k = 4   #簇数
    # area_id = 2309   #地区id
    # start_time = '2020-10-1'   #开始时间
    # end_time = '2020-12-1'     #结束时间

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
            data1[i][j] = SM4.decrypt(key,data1[i][j])

    test2=True
    if test2:
        # X,y=make_blobs(centers=2, random_state=42)
        labels=LSC_Clustering(data1, k, 4, "Kmeans", 4, 0.5).labels_
        print('result', labels)
